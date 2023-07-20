package com.example.quoridor.utils

interface AfterTask {
    fun ifSuccess(result: Any?)
    fun ifFail(result: Any?)
}