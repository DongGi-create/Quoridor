package com.example.quoridor.communication.retrofit.util

import com.example.quoridor.communication.retrofit.HttpResult

class SuccessfulHttpResult<T>(
    val afterSuccess: (T) -> Unit
): HttpResult<T> {
    override fun success(data: T) {
        afterSuccess(data)
    }

    override fun appFail() {

    }

    override fun fail(throwable: Throwable) {

    }

    override fun finally() {

    }
}