package com.example.quoridor.communication

import com.google.gson.Gson

abstract class Sendable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}