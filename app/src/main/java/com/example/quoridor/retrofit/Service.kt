package com.example.quoridor.retrofit

import android.util.Log

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

class Service {

    companion object {
        private val client = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .build()
        private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://43.201.189.249:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var service = retrofit.create(UserService::class.java)

        private val TAG = "Dirtfy Test"

    }

    fun login(
        id: String,
        pw: String,
        httpResult: HttpResult<DTO.SignUpResponse>
    ) {
        val body = DTO.Login(id, pw)
        service.login(body).enqueue(makeCallBack(httpResult))
    }

    fun signUp(
        loginId: String,
        password: String,
        email: String,
        name: String,
        httpResult: HttpResult<DTO.SignUpResponse>
    ) {
        val body = DTO.SignUpRequest(loginId, password, email, name)
        service.signUp(body).enqueue(makeCallBack(httpResult))
    }

    fun match(
        uid: Long,
        gameType: Int,
        httpResult: HttpResult<DTO.MatchingResponse>
    ) {
        val body = DTO.MatchingRequest(uid, gameType)
        service.matchRequest(body).enqueue(makeCallBack(httpResult))
    }
    fun makeMatchCall(data: DTO.MatchingRequest): Call<DTO.MatchingResponse> {
        return service.matchRequest(data)
    }

    private fun <ResponseType> makeCallBack(
        httpResult: HttpResult<ResponseType>
    ): Callback<ResponseType> {
        return object : Callback<ResponseType> {
            override fun onResponse(
                call: Call<ResponseType>,
                response: Response<ResponseType>
            ) {//일을 집어넣어주고
                if (response.isSuccessful) {//성공이되면
                    val header = response.headers()
                    val result = response.body()//값을 받아옴

                    if (result != null) {
                        Log.d(TAG, "response successful\n$header\n$result")
                        httpResult.success(result)
                    } else {
                        Log.d(TAG, "response fail")
                        httpResult.appFail()
                    }
                } else {
                    Log.d(TAG, "response fail")
                    httpResult.appFail()
                }
                httpResult.finally()
            }

            override fun onFailure(call: Call<ResponseType>, t: Throwable) {
                Log.d(TAG, "onFail " + t.message.toString())
                httpResult.fail(t)
                httpResult.finally()
            }
        }
    }

}