package com.dotanphu.sipapp.ui.contact

import androidx.lifecycle.MutableLiveData
import com.dotanphu.sipapp.component.base.BaseViewModel
import com.dotanphu.sipapp.data.model.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor() : BaseViewModel() {
    var onUsersSuccess: MutableLiveData<List<User>> = MutableLiveData()

    fun getListUsers() {
        val d: Disposable = dataManager.mApiHelper.getListUsers()
            .compose(schedulerProvider.ioToMainSingle())
            .subscribeWith(object : DisposableSingleObserver<List<User>>() {
                override fun onSuccess(user: List<User>) {
                    onUsersSuccess.postValue(user)
                }

                override fun onError(e: Throwable) {
                    handleTokenRefreshException(e)
                }
            })
        compositeDisposable.add(d)
    }
}