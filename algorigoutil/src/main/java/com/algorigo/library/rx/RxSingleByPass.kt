package com.algorigo.library.rx

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

fun <T> Single<T>.andThenSingle(completable: Completable) : Single<T> {
    return flatMap {
        completable
            .andThen(Single.just(it))
    }
}