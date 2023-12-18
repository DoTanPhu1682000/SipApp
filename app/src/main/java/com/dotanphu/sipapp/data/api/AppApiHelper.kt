package com.dotanphu.sipapp.data.api

import com.androidnetworking.error.ANError
import com.dotanphu.sipapp.data.model.response.Login
import com.dotanphu.sipapp.data.model.response.User
import com.dotanphu.sipapp.data.prefs.AppPreferenceHelper
import com.exception.TokenRefreshException
import com.rx3androidnetworking.Rx3AndroidNetworking
import com.utils.LogUtil
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.functions.Supplier
import org.json.JSONObject
import java.net.HttpURLConnection
import javax.inject.Inject

class AppApiHelper @Inject constructor(val appPreferenceHelper: AppPreferenceHelper) : ApiHelper {

    private val APPLICATION_JSON = "application/json"
    private val APPLICATION_FORM_URL_ENCODE = "application/x-www-form-urlencoded"
    private val KEY_CONTENT_TYPE = "Content-Type"
    private val KEY_AUTHORIZATION = "Authorization"
    private val KEY_LANGUAGE = "lang"
    private val KEY_PAGE_SIZE = "page_size"
    private val KEY_PAGE_NUMBER = "page_number"

    private fun getKeyAuthorization(): String {
        //for test with password
        //return "Basic bWVkaWhvbWU6bWVkaWhvbWVAMTIzNEAjJA==" //Medihome
        return "Basic ZmY1NmI3Yzg3YzRiOGY1Nzk0ZjhjZmU4NjYwYzcxM2ZjMWY1YWVlZjQzNWIxZTAzNzZhYjMxYzBkM2RkZjQ4MjphODMzZGU1MmIzNjIzM2ZkMDAxODkzMjQxNzJlMTYyOQ=="
    }

    override fun getUserInfo(): Single<Login> {
        return Single.defer(Supplier<SingleSource<Login>> {
            Rx3AndroidNetworking.get(ApiEndPoint.USER_INFO)
                .addHeaders(KEY_CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(KEY_AUTHORIZATION, appPreferenceHelper.token)
                .build()
                .getObjectSingle(Login::class.java)
        }).compose(autoRefreshTokenOnce())
    }

    override fun getListUsers(): Single<List<User>> {
        return Single.defer(Supplier<SingleSource<List<User>>> {
            Rx3AndroidNetworking.get(ApiEndPoint.USERS)
                .addHeaders(KEY_CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(KEY_AUTHORIZATION, appPreferenceHelper.token)
                .build()
                .getObjectListSingle(User::class.java)
        }).compose(autoRefreshTokenOnce())
    }

    override fun sendNotificationFcmDirect(tokenFCM: String, title: String, body: String): Single<JSONObject> {
        return Single.defer {
            val notification = JSONObject()
            notification.put("title", title)
            notification.put("body", body)

            val requestData = JSONObject()
            requestData.put("to", tokenFCM)
            requestData.put("notification", notification)

            Rx3AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                .addHeaders(KEY_CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(KEY_AUTHORIZATION, "key=AAAAdn4v0QA:APA91bEhkIeNdRVH3Cy7anUgLos0soJtGk_WP_wOdjYIx5Rv2FkYlT6MEUg_Gng6ic8y7qB7sa-9Wj0zSHov5W4sQKEqupTdPM1EZ0W5x7ceS5U-NpnC62dEjnRsG2-HcbFyEncEsTAz")
                .addJSONObjectBody(requestData)
                .build()
                .jsonObjectSingle
        }.compose(autoRefreshTokenOnce())
    }

    /*----------------------------------[ACCOUNT]-------------------------------------------------*/
    override fun login(phone: String, password: String): Single<Login> {
        return Rx3AndroidNetworking.post(ApiEndPoint.LOGIN)
            .addUrlEncodeFormBodyParameter("username", phone)
            .addUrlEncodeFormBodyParameter("password", password)
            .addUrlEncodeFormBodyParameter("grant_type", "password")
            .addHeaders(KEY_CONTENT_TYPE, APPLICATION_FORM_URL_ENCODE)
            .addHeaders(KEY_AUTHORIZATION, getKeyAuthorization())
            .build()
            .getObjectSingle(Login::class.java)
    }

    /*----------------------------------[TOKEN]---------------------------------------------------*/
    private fun doRefreshToken(): Completable {
        //Chú ý: Đây là Token, không phải Pre-Token
        return refreshToken() //Nếu gặp bất cứ lỗi gì [400,401,403], return TokenRefreshException
            .onErrorResumeNext(Function<Throwable, SingleSource<Login>> { throwable -> //400,401,403
                if (throwable is ANError) {
                    LogUtil.wtf("doRefreshToken: %s", throwable.errorCode)
                    when (throwable.errorCode) {
                        HttpURLConnection.HTTP_BAD_REQUEST, HttpURLConnection.HTTP_UNAUTHORIZED ->                                     //case HTTP_FORBIDDEN:
                            return@Function Single.error<Login>(TokenRefreshException())

                        else -> {}
                    }
                }
                // If the error was not 401, pass through the original error
                Single.error<Login>(throwable)
            }) // We don't need the result of getAccessToken() any more, so I
            // think it's cleaner to convert the stream to a Completable.
            .ignoreElement()
    }

    fun autoRefreshTokenOnceCompletable(): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream.onErrorResumeNext(Function<Throwable, CompletableSource> { throwable ->
                if (throwable is ANError) {
                    if (throwable.errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        return@Function doRefreshToken() // After the token is refreshed and stored, the original request
                            // should be repeated.
                            .andThen(upstream)
                    }
                }
                // If the error was not 401, pass through the original error
                Completable.error(throwable)
            })
        }
    }

    fun <T : Any> autoRefreshTokenOnce(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.onErrorResumeNext(Function<Throwable, SingleSource<T>> { throwable ->
                if (throwable is ANError) {
                    if (throwable.errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        return@Function doRefreshToken() // After the token is refreshed and stored, the original request
                            // should be repeated.
                            .andThen<T>(upstream)
                    }
                }
                // If the error was not 401, pass through the original error
                Single.error<T>(throwable)
            })
        }
    }

    fun refreshToken(): Single<Login> {
        return Rx3AndroidNetworking.post(ApiEndPoint.LOGIN)
            .addHeaders(KEY_AUTHORIZATION, getKeyAuthorization())
            .addHeaders(KEY_CONTENT_TYPE, APPLICATION_FORM_URL_ENCODE)
            .addUrlEncodeFormBodyParameter("grant_type", "refresh_token")
            .addUrlEncodeFormBodyParameter("refresh_token", appPreferenceHelper.refreshToken)
            .build()
            .getObjectSingle(Login::class.java) // I always use doOnSuccess() for non-Rx side effects, such as caching the token.
            // I think it's clearer than doing the caching in a map() or flatMap()
            .doOnSuccess(Consumer<Login> { login -> // Save the access token to the store for later use.
                appPreferenceHelper.saveLoginInfo(login)
            })
    }

    fun saveLoginInfoTransformer(phone: String, password: String, loginType: String): SingleTransformer<Login, Login> {
        return SingleTransformer<Login, Login> { upstream ->
            upstream.doOnSuccess { login ->
                appPreferenceHelper.saveLoginInfo(login)
                appPreferenceHelper.loginPhone = phone
            }
        }
    }
}