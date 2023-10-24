package com.dotanphu.sipapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.data.model.response.Login
import com.dotanphu.sipapp.databinding.ActivityTestBinding
import com.dotanphu.sipapp.ui.call.IncomingCallBKActivity
import com.dotanphu.sipapp.ui.call.OutgoingCallActivityBK
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private var phone: String = ""
    private var password: String = ""

    @Inject
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listener()
    }

    private fun listener() {
        binding.bLogin.setOnClickListener {
            phone = binding.edtPhone.text.toString()
            password = binding.edtPassword.text.toString()
            dataManager.mApiHelper.login(phone, password)
                .doOnSuccess {
                    dataManager.mPreferenceHelper.saveLoginInfo(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<Login> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(s: Login) {
                        LogUtil.wtf("login")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        }
        binding.bRefreshToken.setOnClickListener {
            dataManager.mApiHelper.refreshToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<Login> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(s: Login) {
                        LogUtil.wtf("refreshToken")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        }
        binding.bApi.setOnClickListener {
            dataManager.mApiHelper.getUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<Login> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(t: Login) {
                        LogUtil.wtf("api getUserInfo")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        }
        binding.goToInComing.setOnClickListener {
            val intent = Intent(this, IncomingCallBKActivity::class.java)
            startActivity(intent)
        }
        binding.goToOutGoing.setOnClickListener {
            val intent = Intent(this, OutgoingCallActivityBK::class.java)
            startActivity(intent)
        }
    }
}