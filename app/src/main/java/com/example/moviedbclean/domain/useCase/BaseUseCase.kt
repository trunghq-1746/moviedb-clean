package com.example.moviedbclean.domain.useCase

import com.example.moviedbclean.domain.mapper.mapToErrorDomainModel
import com.example.moviedbclean.domain.response.ErrorDomainModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseUseCase<T,O> {

    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseObservable(): O

    abstract fun execute(block: OnCompletionCallback<T>.() -> Unit)

    protected fun executeSingle(block: OnCompletionCallback<T>.() -> Unit) {
        val callback = OnCompletionCallback<T>().apply { block() }
        val disposable = (buildUseCaseObservable() as Single<T>)
            .doOnSubscribe {
                callback(true)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    callback(false)
                    callback(it)
                },
                {
                    callback(false)
                    callback(it.mapToErrorDomainModel())
                }
            )
        addDisposable(disposable)
    }

    protected fun executeCompletable(block: OnCompletionCallback<T>.() -> Unit) {
        val callback = OnCompletionCallback<T>().apply { block() }
        val disposable = (buildUseCaseObservable() as Completable)
            .doOnSubscribe {
                callback(true)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    callback(false)
                },
                {
                    callback(false)
                    callback(it.mapToErrorDomainModel())
                }
            )
        addDisposable(disposable)
    }

    protected fun executeObservable(block: OnCompletionCallback<T>.() -> Unit) {
        val callback = OnCompletionCallback<T>().apply { block() }
        val disposable = (buildUseCaseObservable() as Observable<T>)
            .doOnSubscribe {
                callback(true)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    callback(false)
                    callback(it)
                },
                {
                    callback(false)
                    callback(it.mapToErrorDomainModel())
                },
                {
                    callback(false)
                }
            )
        addDisposable(disposable)
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    class OnCompletionCallback<T> {
        private var onLoading: ((isLoading: Boolean, progress: Int?) -> Unit)? = null
        private var onComplete: ((result: T) -> Unit)? = null
        private var onError: ((error: ErrorDomainModel) -> Unit)? = null

        fun onLoading(block: (isLoading: Boolean, progress: Int?) -> Unit) {
            onLoading = block
        }

        fun onComplete(block: (result: T) -> Unit) {
            onComplete = block
        }

        fun onError(block: (error: ErrorDomainModel) -> Unit) {
            onError = block
        }

        operator fun invoke(isLoading: Boolean, progress: Int? = null) {
            onLoading?.invoke(isLoading, progress)
        }

        operator fun invoke(result: T) {
            onComplete?.invoke(result)
        }

        operator fun invoke(error: ErrorDomainModel) {
            onError?.invoke(error)
        }
    }
}