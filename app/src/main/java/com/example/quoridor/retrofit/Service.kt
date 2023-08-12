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

    fun login(id: String, pw: String, httpResult: HttpResult<DTO.Login>) {

        val body = DTO.Login(id, pw)

        service.login(body).enqueue(object: Callback<DTO.Login> {
            override fun onResponse(call: Call<DTO.Login>, response: Response<DTO.Login>) {
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

            override fun onFailure(call: Call<DTO.Login>, t: Throwable) {
                Log.d(TAG, "onFail " + t.message.toString())
                httpResult.fail(t)
            }
        })
    }
}