package com.example.quoridor.communication.socket

import com.example.quoridor.communication.Sendable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

object DTO {

    data class Data(
        @SerializedName("remainTime")
        val remainTime: Long,
        @SerializedName("type")
        val type: Int,
        @SerializedName("row")
        val row: Int,
        @SerializedName("col")
        val col: Int
    ): Serializable, Sendable()

}