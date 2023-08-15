package com.example.quoridor.retrofit.util

import android.content.Context
import android.util.Log
import com.example.quoridor.retrofit.HttpResult
import com.example.quoridor.retrofit.util.Func.Companion.popToast

class ToastHttpResult<T>(
    private val context: Context,
    private val str: String,
    val tag: String
): HttpResult<T> {
    override fun success(data: T) {
        popToast(context, "$str success")

        Log.d(tag, "$str success, data: $data")
    }

    override fun appFail() {
        popToast(context, "app fail")

        Log.d(tag, "$str app fail")
    }

    override fun fail(throwable: Throwable) {
        popToast(context, "fail")

        Log.d(tag, "$str fail, error: ${throwable.message}")
    }

    override fun finally() {
        Log.d(tag, "$str end")
    }
}