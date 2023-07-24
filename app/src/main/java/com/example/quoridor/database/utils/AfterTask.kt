package com.example.quoridor.database.utils

interface AfterTask {
    fun ifSuccess(result: Any?)
    fun ifFail(result: Any?)
}