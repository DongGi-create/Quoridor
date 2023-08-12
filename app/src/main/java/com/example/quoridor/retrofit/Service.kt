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
        private var service = retrofit.create(UserService::class.java)

        private val TAG = "Dirtfy Test"

    }

    fun login(id: String, pw: String, httpResult: HttpResult<DTO.SignUpResponse>) {

        val body = DTO.Login(id, pw)

        service.login(body).enqueue(object: Callback<DTO.SignUpResponse> {
            override fun onResponse(call: Call<DTO.SignUpResponse>, response: Response<DTO.SignUpResponse>) {
                if (response.isSuccessful) {
                    val header = response.headers()
                    val result = response.body()

                    if (result != null) {
                        Log.d(TAG, "response successful\n$header\n${result?.toString()}")
                        httpResult.success(result)
                    }
                    else {
                        Log.d(TAG, "response fail")
                        httpResult.appFail()
                    }
                } else {
                    Log.d(TAG, "response fail")
                    httpResult.appFail()
                }
            }

            override fun onFailure(call: Call<DTO.SignUpResponse>, t: Throwable) {
                Log.d(TAG, "onFail " + t.message.toString())
                httpResult.fail(t)
            }
        })
    }

    fun match(uid:Long, gameType:Int, httpResult: HttpResult<DTO.MatchingResponse>) {
        val body = DTO.MatchingRequest(uid, gameType)
        service.matchRequest(body).enqueue(object: Callback<DTO.MatchingResponse> {
            override fun onResponse(call: Call<DTO.MatchingResponse>, response: Response<DTO.MatchingResponse>) {//일을 집어넣어주고
                if(response.isSuccessful) {//성공이되면
                    val header = response.headers()//값을 받아옴
                    val result = response.body()

                    if (result != null) {
                        Log.d(TAG, "response successful\n$header\n${result?.toString()}")
                        httpResult.success(result)
                    }
                    else {
                        Log.d(TAG, "response fail")
                        httpResult.appFail()
                    }
                } else {
                    Log.d(TAG, "response fail")
                    httpResult.appFail()
                }
            }
            override fun onFailure(call: Call<DTO.MatchingResponse>, t: Throwable) {
                Log.d(TAG, "onFail " + t.message.toString())
                httpResult.fail(t)
            }
        })
    }


}