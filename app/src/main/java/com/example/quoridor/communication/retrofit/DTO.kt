package com.example.quoridor.communication.retrofit

import com.google.gson.annotations.SerializedName

object DTO {
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
        val score: String,
        @SerializedName("uid")
        val uid: String
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
}
