package com.example.quoridor.communication.retrofit

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
    @POST("users/help/pw")
    fun getNewPw(@Body data: HttpDTO.Request.NewPw): Call<HttpDTO.Response.NewPw>

    @Headers(DEFAULT_HEADER)
    @POST("users")
    fun signUp(@Body data: HttpDTO.Request.Signup): Call<HttpDTO.Response.User>

    @Headers(DEFAULT_HEADER)
    @POST("matched_users")
    fun matchRequest(@Body data: HttpDTO.Request.Match): Call<String?>

    @Headers(DEFAULT_HEADER)
    @GET("matched_users")
    fun matchCheckRequest(): Call<HttpDTO.Response.Match?>

    @Headers(DEFAULT_HEADER)
    @DELETE("matched_users")
    fun exitMatching(@Query("gameType") gameType: Int): Call<HttpDTO.Response.Match?>
    //get이나 delete는 dto가 아니고 그냥 Query로 해서 주소뒤에 붙여서 준다
    @Headers(DEFAULT_HEADER)
    @POST("users/update")
    fun userUpdate(@Body data: HttpDTO.Request.UserUpdate): Call<HttpDTO.Response.User>

    @Headers(DEFAULT_HEADER)
    @GET("users/logout")
    fun logout(): Call<String?>

    @Headers(DEFAULT_HEADER)
    @GET("histories")
    fun historyListRequest(@Query("recentGameId") recentGameId: Long): Call<List<HttpDTO.Response.CompHistory>>

    @Headers(DEFAULT_HEADER)
    @GET("histories/{gameId}")
    fun historyDetailRequest(@Path("gameId") gameId: Long): Call<HttpDTO.Response.DetailHistory>

    @GET("kakao/callback")
    fun loginByKakao(@Query("code") code: String): Call<HttpDTO.Response.User>

    @GET("ranking")
    fun adjacentRanking(): Call<HttpDTO.Response.Rank>

    @GET("ranking/under")
    fun underRanking(@Query("uid") uid: Long): Call<HttpDTO.Response.Rank>

    @GET("ranking/over")
    fun overRanking(@Query("uid") uid: Long): Call<HttpDTO.Response.Rank>

    @GET("profile/delete")
    fun deleteImage(): Call<String>

    @Headers(DEFAULT_HEADER)
    @GET("profile")
    fun getImage(@Query("uid") uid: Long): Call<String>

    @Multipart
    @POST("profile")
    fun uploadImage(@Part image: MultipartBody.Part?): Call<String>
}