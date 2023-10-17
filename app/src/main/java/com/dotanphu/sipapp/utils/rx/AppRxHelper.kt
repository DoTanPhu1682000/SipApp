package com.dotanphu.sipapp.utils.rx

import com.custom.ListOfSomething
import com.exception.ParseException
import com.google.gson.Gson
import com.utils.ParseUtil
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.functions.Function
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import javax.inject.Inject

class AppRxHelper @Inject constructor() : RxHelper {

    override fun <T> handleApiToListObject(type: Class<T>): SingleTransformer<JSONObject, List<T>> {
        return SingleTransformer<JSONObject, List<T>> { upstream ->
            upstream.flatMap(Function<JSONObject, SingleSource<List<T>>> { jsonObject ->
                val status = ParseUtil.getIntJson("statuscode", jsonObject)
                when (status) {
                    HttpURLConnection.HTTP_OK -> Single.just(jsonObject)
                        .compose(toListObject("data", type))

                    HttpURLConnection.HTTP_NOT_FOUND -> Single.just(ArrayList())
                    else -> Single.error(ParseException(ParseUtil.getStringJson("description", jsonObject)))
                }
            })
        }
    }

    override fun <T> toListObject(keyJSONArray: String, type: Class<T>): SingleTransformer<JSONObject, List<T>> {
        return SingleTransformer<JSONObject, List<T>> { upstream ->
            upstream.map(Function { jsonObject ->
                try {
                    val contents = ParseUtil.getJSONArray(keyJSONArray, jsonObject)
                    if (contents != null) return@Function Gson().fromJson<List<T>>(contents.toString(), ListOfSomething<T>(type))
                } catch (ignore: Exception) {
                    //ignore
                }
                throw ParseException()
            })
        }
    }

    override fun <T : Any> toObject(keyJSONObject: String, type: Class<T>): SingleTransformer<JSONObject, T> {
        return SingleTransformer<JSONObject, T> { upstream ->
            upstream.map(Function { jsonObject ->
                try {
                    val `object` = ParseUtil.getJSONObject(keyJSONObject, jsonObject)
                    if (`object` != null) return@Function Gson().fromJson<T>(`object`.toString(), type)
                } catch (ignore: Exception) {
                    //ignore
                }
                throw ParseException()
            })
        }
    }

    override fun <T> toListObject(type: Class<T>): SingleTransformer<JSONArray, List<T>> {
        return SingleTransformer<JSONArray, List<T>> { upstream ->
            upstream.map(Function<JSONArray, List<T>> { jsonArray ->
                Gson().fromJson(jsonArray.toString(), ListOfSomething(type))
            })
        }
    }
}