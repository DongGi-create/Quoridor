package com.example.quoridor.communication.retrofit

import com.google.gson.annotations.SerializedName

object HttpDTO {
    data class Login(
        @SerializedName("loginId")
        val loginId: String,
        @SerializedName("password")
        val password: String
    )

    data class SignUpRequest(
        @SerializedName("loginId")
        val loginId: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("name")
        val name: String
    )

    data class SignUpResponse(
        @SerializedName("loginId")
        val loginId: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("score")
        val score: Int,
        @SerializedName("uid")
        val uid: Long
    )

    data class MatchingRequest(
        @SerializedName("gameType")
        val gameType: Int
    )

    data class MatchingResponse(
        @SerializedName("gameId")
        val gameId: String?,
        @SerializedName("turn")
        val turn: Int?
    )

    data class HistoriesRequest(
        @SerializedName("recentGameId")
        val recentGameId: Long
    )

    data class HistoriesResponse(
        @SerializedName("gameId")
        val gameId: Long,
        @SerializedName("win")
        val win: Boolean,
        @SerializedName("opponentName")
        val opponentName: String
    )
}
