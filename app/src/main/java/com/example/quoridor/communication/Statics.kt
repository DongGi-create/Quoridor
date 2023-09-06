package com.example.quoridor.communication

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

object Statics {
    private const val HTTP = "http://"
    private const val WEB_SOCKET = "ws://"

    private const val IP = "43.201.189.249"
    const val PORT = 8080

    const val HTTP_BASE_URL = "$HTTP$IP:$PORT"
    const val WEB_SOCKET_BASE_URL = "$WEB_SOCKET$IP:$PORT"

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .pingInterval(60, TimeUnit.SECONDS)
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(HTTP_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}