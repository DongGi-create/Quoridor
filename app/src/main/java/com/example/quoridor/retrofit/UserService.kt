package com.example.quoridor.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @Headers(
        "Postman-Token: <calculate when request is sent>;" +
                "Content-Type: application/json;" +
                "Content-Length: <calculate when request is sent>;" +
                "Host: <calculate when request is sent>;" +
                "User-Agent: PostmanRuntime/7.32.3;" +
                "Accept-Encoding: gzip, deflate, br;" +
                "Connection: keep-alive;" +
                "Accept: application/json"
    )
    @POST("users/login")
    fun login(@Body data: DTO.Login): Call<DTO.Login>

    @Headers(
        "Postman-Token: <calculate when request is sent>;" +
                "Content-Type: application/json;" +
                "Content-Length: <calculate when request is sent>;" +
                "Host: <calculate when request is sent>;" +
                "User-Agent: PostmanRuntime/7.32.3;" +
                "Accept-Encoding: gzip, deflate, br;" +
                "Connection: keep-alive;" +
                "Accept: application/json"
    )
    @POST("users")
    fun signUp(@Body data: DTO.SignUpRequest): Call<DTO.SignUpResponse>
}