package com.example.quoridor.retrofit

import com.google.gson.annotations.SerializedName

data class LoginDTO(
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("password")
    val password: String
)
