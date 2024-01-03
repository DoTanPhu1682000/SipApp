package com.vegastar.sipapp.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.vegastar.sipapp.component.base.BaseViewModel
import com.vegastar.sipapp.data.model.response.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import javax.inject.Inject

@HiltViewModel
class AccountLoginViewModel @Inject constructor() : BaseViewModel() {
    var onLoginSuccess: MutableLiveData<Login> = MutableLiveData()

    fun doLogin(context: Context, phone: String, password: String) {
        val d: Disposable = dataManager.mApiHelper.login(phone, password)
            .doOnSuccess {
                dataManager.mPreferenceHelper.saveLoginInfo(it)
            }
            .compose(schedulerProvider.ioToMainSingle())
            .subscribeWith(object : DisposableSingleObserver<Login>() {
                override fun onSuccess(login: Login) {
                    onLoginSuccess.postValue(login)
                }

                override fun onError(e: Throwable) {
                    handleTokenRefreshException(e)
                }
            })
        compositeDisposable.add(d)
    }
}