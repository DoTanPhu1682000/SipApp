package com.vegastar.sipapp.utils.rx

import com.vegastar.sipapp.component.base.BaseViewModel
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.MaybeTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.SingleTransformer

interface SchedulerProvider {
    fun ui(): Scheduler?
    fun computation(): Scheduler?
    fun io(): Scheduler?

    //BASIC
    fun <T : Any> ioToMainSingle(): SingleTransformer<T, T>?
    fun <T : Any> ioToMainMaybe(): MaybeTransformer<T, T>?
    fun ioToMainCompletable(): CompletableTransformer?
    fun <T : Any> ioToMainObservable(): ObservableTransformer<T, T>?

    //SINGLE
    fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?, requestCode: Int): SingleTransformer<T, T>?
    fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?, requestCode: Int, text: String?): SingleTransformer<T, T>?
    fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?): SingleTransformer<T, T>?
    fun <T : Any> ioToMainLoadingSingle(viewModel: BaseViewModel?, text: String?): SingleTransformer<T, T>?
    fun <T : Any> ioToMainProgressSingle(viewModel: BaseViewModel?): SingleTransformer<T, T>?
    fun <T : Any> ioToMainProgressSingle(viewModel: BaseViewModel?, text: String?): SingleTransformer<T, T>?

    //COMPLETABLE
    fun ioToMainLoadingCompletable(viewModel: BaseViewModel?, requestCode: Int): CompletableTransformer?
    fun ioToMainLoadingCompletable(viewModel: BaseViewModel?, requestCode: Int, text: String?): CompletableTransformer?
    fun ioToMainLoadingCompletable(viewModel: BaseViewModel?): CompletableTransformer?
    fun ioToMainLoadingCompletable(viewModel: BaseViewModel?, text: String?): CompletableTransformer?
    fun ioToMainProgressCompletable(viewModel: BaseViewModel?): CompletableTransformer?
    fun ioToMainProgressCompletable(viewModel: BaseViewModel?, text: String?): CompletableTransformer?

    //MAYBE
    fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?, requestCode: Int): MaybeTransformer<T, T>?
    fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?, requestCode: Int, text: String?): MaybeTransformer<T, T>?
    fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?): MaybeTransformer<T, T>?
    fun <T : Any> ioToMainLoadingMaybe(viewModel: BaseViewModel?, text: String?): MaybeTransformer<T, T>?
    fun <T : Any> ioToMainProgressMaybe(viewModel: BaseViewModel?): MaybeTransformer<T, T>?
    fun <T : Any> ioToMainProgressMaybe(viewModel: BaseViewModel?, text: String?): MaybeTransformer<T, T>?

    //OBSERVABLE
    fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?, requestCode: Int): ObservableTransformer<T, T>?
    fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?, requestCode: Int, text: String?): ObservableTransformer<T, T>?
    fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?): ObservableTransformer<T, T>?
    fun <T : Any> ioToMainLoadingObservable(viewModel: BaseViewModel?, text: String?): ObservableTransformer<T, T>?
    fun <T : Any> ioToMainProgressObservable(viewModel: BaseViewModel?): ObservableTransformer<T, T>?
    fun <T : Any> ioToMainProgressObservable(viewModel: BaseViewModel?, text: String?): ObservableTransformer<T, T>?
}