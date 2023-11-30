package com.example.quoridor.communication.retrofit.util

import android.content.Context
import android.util.Log
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.util.Func

class ToastHttpResult<T>(
    private val context: Context,
    private val str: String,
    val tag: String
    ): HttpResult<T> {

    companion object{
        lateinit var data1: HttpDTO.Response.Match
    }
    override fun success(data: T) {
        //Func.popToast(context, "$str success")
        data1 = data as HttpDTO.Response.Match
        Log.d(tag, "$str success, data: $data")
    }

    override fun appFail() {
        //Func.popToast(context, "app fail")

        Log.d(tag, "$str app fail")
    }

    override fun fail(throwable: Throwable) {
        //Func.popToast(context, "fail")

        Log.d(tag, "$str fail, error: ${throwable.message}")
    }

    override fun finally() {
        //Log.d(tag, "$str end")
    }
}