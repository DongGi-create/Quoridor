package com.example.quoridor.communication.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceInterface {

    companion object {
        private const val DEFAULT_HEADER =
            "Postman-Token: <calculate when request is sent>;" +
            "Content-Type: application/json;" +
            "Content-Length: <calculate when request is sent>;" +
            "Host: <calculate when request is sent>;" +
            "User-Agent: PostmanRuntime/7.32.3;" +
            "Accept-Encoding: gzip, deflate, br;" +
            "Connection: keep-alive;" +
            "Accept: application/json"
    }

    @Headers(DEFAULT_HEADER)
    @POST("users/login")
    fun login(@Body data: HttpDTO.Request.Login): Call<HttpDTO.Response.User>

    @Headers(DEFAULT_HEADER)
    @POST("users")
    fun signUp(@Body data: HttpDTO.Request.Signup): Call<HttpDTO.Response.User>

    @Headers(DEFAULT_HEADER)
    @POST("match")
    fun matchRequest(@Body data: HttpDTO.Request.Match): Call<HttpDTO.Response.Match>

    @Headers(DEFAULT_HEADER)
    @POST("users/update")
    fun userUpdate(@Body data: HttpDTO.Request.UserUpdate): Call<HttpDTO.Response.User>

    @Headers(DEFAULT_HEADER)
    @GET("users/logout")
    fun logout(): Call<String>

    @Headers(DEFAULT_HEADER)
    @GET("histories")
    fun historyListRequest(@Query("recentGameId") recentGameId: Long): Call<List<HttpDTO.Response.CompHistory>>

    @Headers(DEFAULT_HEADER)
    @GET("histories/{gameId}")
    fun historyDetailRequest(@Path("gameId") gameId: Long): Call<HttpDTO.Response.DetailHistory>

    @GET("/kakao/callback")
    fun loginByKakao(@Query("code") code: String): Call<HttpDTO.Response.User>

}