package com.example.quoridor.communication.retrofit

import android.util.Log
import com.example.quoridor.communication.Statics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.Calendar

class HttpService {
    companion object {
        private val retrofit = Retrofit.Builder()
            .client(Statics.client)
            .baseUrl(Statics.HTTP_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        private var service: ServiceInterface = retrofit.create(ServiceInterface::class.java)

        private val TAG = "Dirtfy Test - HttpService"

    }

    fun login(
        id: String,
        pw: String,
        httpResult: HttpResult<HttpDTO.Response.User>
    ) {
        val body = HttpDTO.Request.Login(id, pw)
        service.login(body).enqueue(makeCallBack(httpResult))
    }

    fun signUp(
        loginId: String,
        password: String,
        email: String,
        name: String,
        httpResult: HttpResult<HttpDTO.Response.User>
    ) {
        val body = HttpDTO.Request.Signup(loginId, password, email, name)
        service.signUp(body).enqueue(makeCallBack(httpResult))
    }

    fun userUpdate(
        password: String?,
        email: String?,
        name: String?,
        httpResult: HttpResult<HttpDTO.Response.User>
    ) {
        val body = HttpDTO.Request.UserUpdate(password,email, name)
        service.userUpdate(body).enqueue(makeCallBack(httpResult))
    }

    fun match(
        gameType: Int,
        httpResult: HttpResult<HttpDTO.Response.Match>
    ) {
        val body = HttpDTO.Request.Match(gameType)
        service.matchRequest(body).enqueue(makeCallBack(httpResult))
    }
    fun makeMatchCall(data: HttpDTO.Request.Match): Call<HttpDTO.Response.Match> {
        return service.matchRequest(data)
    }

    fun histories(
        recentGameId: Long,
        httpResult: HttpResult<List<HttpDTO.Response.CompHistory>>
    ) {
        service.historyListRequest(recentGameId).enqueue(makeCallBack(httpResult))
    }
    fun recentHistories(
        httpResult: HttpResult<List<HttpDTO.Response.CompHistory>>
    ) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 1)
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)+1
        val day = cal.get(Calendar.DATE)
        val recentGameId = ((year*10000 + month*100 + day)*(10e+11)).toLong()
        service.historyListRequest(recentGameId).enqueue(makeCallBack(httpResult))
    }

    fun historyDetail(
        gameId: Long,
        httpResult: HttpResult<HttpDTO.Response.DetailHistory>
    ) {
        service.historyDetailRequest(gameId).enqueue(makeCallBack(httpResult))
    }

    fun loginByKakao(
        code: String,
        httpResult: HttpResult<HttpDTO.Response.User>
    ) {
        service.loginByKakao(code).enqueue(makeCallBack(httpResult))
    }

    fun adjacentRanking(
        httpResult: HttpResult<HttpDTO.Response.Rank>
    ) {
        service.adjacentRanking().enqueue(makeCallBack(httpResult))
    }

    fun underRanking(
        uid: Long,
        httpResult: HttpResult<HttpDTO.Response.Rank>
    ) {
        service.underRanking(uid).enqueue(makeCallBack(httpResult))
    }

    fun overRanking(
        uid: Long,
        httpResult: HttpResult<HttpDTO.Response.Rank>
    ) {
        service.overRanking(uid).enqueue(makeCallBack(httpResult))
    }
    fun uploadImage(
        image: MultipartBody.Part?,
        httpResult: HttpResult<String>
    ){
        service.uploadImage(image).enqueue(makeCallBack(httpResult))
    }

    fun delImage(
        httpResult: HttpResult<String>
    ){
        service.deleteImage().enqueue(makeCallBack(httpResult))
    }

    fun getImage(
        uid: Long,
        httpResult: HttpResult<String>
    ){
        service.getImage(uid).enqueue(makeCallBack(httpResult))
    }

    private fun <ResponseType> makeCallBack(
        httpResult: HttpResult<ResponseType>
    ): Callback<ResponseType> {
        return object : Callback<ResponseType> {
            override fun onResponse(
                call: Call<ResponseType>,
                response: Response<ResponseType>
            ) {//일을 집어넣어주고
                Log.d(TAG, "${response.code()}\n${response.headers()}\n${response.body()}")

                if (response.isSuccessful) {//성공이되면
                    val result = response.body()//값을 받아옴

                    if (result != null) {
                        Log.d(TAG, "response successful")
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