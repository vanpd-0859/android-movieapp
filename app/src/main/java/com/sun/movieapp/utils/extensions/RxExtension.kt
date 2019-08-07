package com.sun.movieapp.utils.extensions

import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.widget.SearchView
import io.reactivex.Maybe
import java.lang.NullPointerException

fun Completable.async() =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.async() =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.async() =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.loading(liveData: MutableLiveData<Boolean>) =
    doOnSubscribe { liveData.value = true }.doFinally { liveData.value = false }

fun Completable.loading(liveData: MutableLiveData<Boolean>) =
    doOnSubscribe { liveData.value = true }.doFinally { liveData.value = false }

fun <T> Single<T>.loading(block: (Boolean) -> Unit) =
    doOnSubscribe { block(true) }.doFinally { block(false) }

fun Completable.loading(block: (Boolean) -> Unit) =
    doOnSubscribe { block(true) }.doFinally { block(false) }

fun View.click(): Observable<Unit> {
    return Observable.create { emitter ->
        emitter.setCancellable {
            this.setOnClickListener(null)
            emitter.onComplete()
        }
        this.setOnClickListener { emitter.onNext(Unit) }
    }
}

fun SearchView.listen(): Observable<String> {
    return Observable.create { emitter ->
        emitter.setCancellable {
            this.setOnQueryTextListener(null)
            emitter.onComplete()
        }
        this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                emitter.onNext(text)
                return true
            }
            override fun onQueryTextChange(text: String): Boolean {
                emitter.onNext(text)
                return true
            }
        })
    }
}

fun <T> Single<T>.mapToUnit() = map { Unit }
fun <T> Observable<T>.mapToUnit() = map { Unit }
fun <T> Maybe<T>.mapToUnit() = map { Unit }

fun <T> Single<T>.catchNullException() = onErrorResumeNext {
    when(it) {
        is NullPointerException -> Single.never()
        else -> Single.error(it)
    }
}

fun <T> Observable<T>.catchNullException() = onErrorResumeNext { it: Throwable ->
    when(it) {
        is NullPointerException -> Observable.empty()
        else -> Observable.error(it)
    }
}

fun <T> Maybe<T>.catchNullException() = onErrorResumeNext { it: Throwable ->
    when(it) {
        is NullPointerException -> Maybe.empty()
        else -> Maybe.error(it)
    }
}



