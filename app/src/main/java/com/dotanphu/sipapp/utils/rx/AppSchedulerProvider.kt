package com.dotanphu.sipapp.utils.rx

import android.text.TextUtils
import com.dotanphu.sipapp.component.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.MaybeTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AppSchedulerProvider @Inject constructor() : SchedulerProvider {
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    //BASIC
    override fun <T : Any> ioToMainSingle(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.subscribeOn(io()).observeOn(ui())
        }
    }

    override fun <T : Any> ioToMainMaybe(): MaybeTransformer<T, T> {
        return MaybeTransformer { upstream ->
            upstream.subscribeOn(io()).observeOn(ui())
        }
    }

    override fun ioToMainCompletable(): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream.subscribeOn(io()).observeOn(ui())
        }
    }

    override fun <T : Any> ioToMainObservable(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(io()).observeOn(ui())
        }
    }

    //SINGLE
    override fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?, requestCode: Int): SingleTransformer<T, T> {
        return ioToMainLoadingSingle(viewModel, requestCode, null)
    }

    override fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?, requestCode: Int, text: String?): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.compose(ioToMainSingle<T>())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowLoading(requestCode)
                    }
                }
                .doOnSuccess { if (viewModel != null) viewModel.postHideLoading(requestCode) }
                .doOnError { if (viewModel != null) viewModel.postHideLoading(requestCode) }
        }
    }

    override fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?): SingleTransformer<T, T> {
        return ioToMainLoadingSingle(viewModel, 0, null)
    }

    override fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?, text: String?): SingleTransformer<T, T> {
        return ioToMainLoadingSingle(viewModel, 0, text)
    }

    override fun <T : Any> ioToMainProgressSingle(viewModel: BaseViewModel?): SingleTransformer<T, T> {
        return ioToMainProgressSingle(viewModel, null)
    }

    override fun <T : Any> ioToMainProgressSingle(viewModel: BaseViewModel?, text: String?): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.compose(ioToMainSingle<T>())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowProgress()
                        if (!TextUtils.isEmpty(text)) viewModel.postUpdateProgress(text.toString())
                    }
                }
                .doOnSuccess { if (viewModel != null) viewModel.postHideProgress() }
                .doOnError { if (viewModel != null) viewModel.postHideProgress() }
        }
    }

    //COMPLETABLE
    override fun ioToMainProgressCompletable(viewModel: BaseViewModel?): CompletableTransformer {
        return ioToMainProgressCompletable(viewModel, null)
    }

    override fun ioToMainProgressCompletable(viewModel: BaseViewModel?, text: String?): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream.compose(ioToMainCompletable())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowProgress()
                        if (!TextUtils.isEmpty(text)) viewModel.postUpdateProgress(text.toString())
                    }
                }
                .doOnComplete { if (viewModel != null) viewModel.postHideProgress() }
                .doOnError { if (viewModel != null) viewModel.postHideProgress() }
        }
    }

    override fun ioToMainLoadingCompletable(viewModel: BaseViewModel?, requestCode: Int): CompletableTransformer {
        return ioToMainLoadingCompletable(viewModel, requestCode, null)
    }

    override fun ioToMainLoadingCompletable(viewModel: BaseViewModel?, requestCode: Int, text: String?): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream.compose(ioToMainCompletable())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowLoading(requestCode)
                    }
                }
                .doOnComplete { if (viewModel != null) viewModel.postHideLoading(requestCode) }
                .doOnError { if (viewModel != null) viewModel.postHideLoading(requestCode) }
        }
    }

    override fun ioToMainLoadingCompletable(viewModel: BaseViewModel?): CompletableTransformer {
        return ioToMainLoadingCompletable(viewModel, 0, null)
    }

    override fun ioToMainLoadingCompletable(viewModel: BaseViewModel?, text: String?): CompletableTransformer {
        return ioToMainLoadingCompletable(viewModel, 0, text)
    }

    //MAYBE
    override fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?, requestCode: Int): MaybeTransformer<T, T> {
        return ioToMainLoadingMaybe(viewModel, requestCode, null)
    }

    override fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?, requestCode: Int, text: String?): MaybeTransformer<T, T> {
        return MaybeTransformer { upstream ->
            upstream.compose(ioToMainMaybe<T>())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowLoading(requestCode)
                    }
                }
                .doOnSuccess { if (viewModel != null) viewModel.postHideLoading(requestCode) }
                .doOnComplete { if (viewModel != null) viewModel.postHideLoading(requestCode) }
                .doOnError { if (viewModel != null) viewModel.postHideLoading(requestCode) }
        }
    }

    override fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?): MaybeTransformer<T, T> {
        return ioToMainLoadingMaybe(viewModel, 0, null)
    }

    override fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?, text: String?): MaybeTransformer<T, T> {
        return ioToMainLoadingMaybe(viewModel, 0, text)
    }

    override fun <T : Any> ioToMainProgressMaybe(viewModel: BaseViewModel?): MaybeTransformer<T, T> {
        return ioToMainProgressMaybe(viewModel, null)
    }

    override fun <T : Any> ioToMainProgressMaybe(viewModel: BaseViewModel?, text: String?): MaybeTransformer<T, T> {
        return MaybeTransformer { upstream ->
            upstream.compose(ioToMainMaybe<T>())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowProgress()
                        if (!TextUtils.isEmpty(text)) viewModel.postUpdateProgress(text.toString())
                    }
                }
                .doOnSuccess { if (viewModel != null) viewModel.postHideProgress() }
                .doOnComplete { if (viewModel != null) viewModel.postHideProgress() }
                .doOnError { if (viewModel != null) viewModel.postHideProgress() }
        }
    }

    //OBSERVABLE
    override fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?, requestCode: Int): ObservableTransformer<T, T> {
        return ioToMainLoadingObservable(viewModel, requestCode, null)
    }

    override fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?, requestCode: Int, text: String?): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowLoading(requestCode)
                    }
                }
                .doOnComplete { if (viewModel != null) viewModel.postHideLoading(requestCode) }
                .doOnError { if (viewModel != null) viewModel.postHideLoading(requestCode) }
        }
    }

    override fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?): ObservableTransformer<T, T> {
        return ioToMainLoadingObservable(viewModel, 0, null)
    }

    override fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?, text: String?): ObservableTransformer<T, T> {
        return ioToMainLoadingObservable(viewModel, 0, text)
    }

    override fun <T : Any> ioToMainProgressObservable(viewModel: BaseViewModel?): ObservableTransformer<T, T> {
        return ioToMainProgressObservable(viewModel, null)
    }

    override fun <T : Any> ioToMainProgressObservable(viewModel: BaseViewModel?, text: String?): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (viewModel != null) {
                        viewModel.postShowProgress()
                        if (!TextUtils.isEmpty(text)) viewModel.postUpdateProgress(text.toString())
                    }
                }
                .doOnComplete { if (viewModel != null) viewModel.postHideProgress() }
                .doOnError { if (viewModel != null) viewModel.postHideProgress() }
        }
    }
}