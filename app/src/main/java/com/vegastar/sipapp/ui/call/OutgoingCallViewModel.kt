package com.vegastar.sipapp.ui.call

import androidx.lifecycle.MutableLiveData
import com.vegastar.sipapp.component.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class OutgoingCallViewModel @Inject constructor() : BaseViewModel() {
    var onSendNotificationSuccess: MutableLiveData<JSONObject> = MutableLiveData()

    fun sendNotificationFcmDirect(tokenFCM: String, title: String, body: String) {
        val d: Disposable = dataManager.mApiHelper.sendNotificationFcmDirect(tokenFCM, title, body)
            .compose(schedulerProvider.ioToMainSingle())
            .subscribeWith(object : DisposableSingleObserver<JSONObject>() {
                override fun onSuccess(t: JSONObject) {
                    onSendNotificationSuccess.postValue(t)
                }

                override fun onError(e: Throwable) {
                    handleError(e)
                }
            })
        compositeDisposable.add(d)
    }
}