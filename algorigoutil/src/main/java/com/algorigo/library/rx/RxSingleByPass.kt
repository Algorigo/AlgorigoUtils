package com.algorigo.library.rx

import io.reactivex.Completable
import io.reactivex.Single

fun <T> Single<T>.andThenSingle(completable: Completable) : Single<T> {
    return flatMap {
        completable
            .andThen(Single.just(it))
    }
}