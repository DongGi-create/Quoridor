package com.example.quoridor.communication.socket

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.airbnb.lottie.LottieCompositionFactory.fromJson
import com.example.quoridor.communication.Statics
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketService(
    url: String,
    listener: WebSocketListener
) {

    companion object {
        private const val TAG = "Dirtfy test - WebSocketService"
        private const val END_CODE = 1000
        private const val END_MASSAGE = "Game End"
        private val gson = Gson()
    }

    private val ws: WebSocket =
        Statics.client
            .newWebSocket(
                Request.Builder().url(url).build(),
                listener
            )

    fun <T> toJsonString(data: T): String {
        val jsonString = gson.toJson(data)
        Log.d(TAG, jsonString)
        return jsonString
    }

    fun <T> fromJsonString(text: String, cls: Class<T>): T {
        return gson.fromJson(text, cls)
    }

    fun <T> send(data: T) {
        CoroutineScope(Dispatchers.IO)
            .launch {
                ws.send(toJsonString(data))
            }
    }

    fun close() {
        ws.close(END_CODE, END_MASSAGE)
    }

}