package com.example.quoridor.retrofit

import android.util.Log
import com.example.quoridor.R
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
        private val service = retrofit.create(LoginService::class.java)

        private val TAG = "Dirtfy Test"

    }

    fun login(id: String, pw: String, httpResult: HttpResult<LoginDTO>) {

        val body = LoginDTO(id, pw)

        service.login(body).enqueue(object: Callback<LoginDTO> {
            override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
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

            override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                Log.d(TAG, "onFail " + t.message.toString())
                httpResult.fail(t)
            }
        })
    }
}