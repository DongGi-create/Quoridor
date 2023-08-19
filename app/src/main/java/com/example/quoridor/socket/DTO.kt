package com.example.quoridor.socket

import java.io.Serializable

object DTO {
    data class Action(
        val type: Char,
        val row: Char,
        val col: Char
    ): Serializable
    data class Result(
        val action: Action,
        val remainTime: Long,
        val isEnd: Char
    ): Serializable

    data class Data(
        val isEnd: Char,
        val remainTime: Long,
        val type: Char,
        val row: Char,
        val col: Char
    ): Serializable
}