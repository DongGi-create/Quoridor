package com.example.quoridor.deprecated.database.utils

interface AfterTask {
    fun ifSuccess(result: Any?)
    fun ifFail(result: Any?)
}