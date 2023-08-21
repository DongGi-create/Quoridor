package com.example.quoridor.communication.socket

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.util.concurrent.TimeUnit

object WebSocketTest {
    const val BASE_URL = "ws://43.201.189.249:8080/"

    val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .pingInterval(30, TimeUnit.SECONDS)
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()
}