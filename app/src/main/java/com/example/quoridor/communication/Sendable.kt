package com.example.quoridor.communication

import com.google.gson.Gson

abstract class Sendable {
    private val gson = Gson()
    override fun toString(): String {
        return gson.toJson(this)
    }
}