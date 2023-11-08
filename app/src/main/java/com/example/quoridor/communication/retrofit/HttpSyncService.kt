package com.example.quoridor.communication.retrofit

import android.util.Log
import com.example.quoridor.communication.Statics.retrofit
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object HttpSyncService {
    private val service: ServiceInterface = retrofit.create(ServiceInterface::class.java)
    private val TAG = "Dirtfy Test - HttpSyncService"

    suspend fun login(
        id: String,
        pw: String
    ): HttpDTO.Response.User? {
        val body = HttpDTO.Request.Login(id, pw)
        return extractBody(service.login(body).execute())
    }

    suspend fun logout(
    ): String? {
        return extractBody(service.logout().execute())
    }

    suspend fun signUp(
        loginId: String,
        password: String,
        email: String,
        name: String
    ): HttpDTO.Response.User? {
        val body = HttpDTO.Request.Signup(loginId, password, email, name)
        return extractBody(service.signUp(body).execute())
    }

    suspend fun userUpdate(
        password: String?,
        email: String?,
        name: String?
    ): HttpDTO.Response.User? {
        val body = HttpDTO.Request.UserUpdate(password,email, name)
        return extractBody(service.userUpdate(body).execute())
    }

    suspend fun match(
        gameType: Int
    ): String? {
        val body = HttpDTO.Request.Match(gameType)
        return extractBody(service.matchRequest(body).execute())
    }

    suspend fun matchCheck(
    ): HttpDTO.Response.Match? {
        return extractBody(service.matchCheckRequest().execute())
    }

    suspend fun exitMatching(
        gameType: Int
    ): HttpDTO.Response.Match? {
        return extractBody(service.exitMatching(gameType).execute())
    }

    suspend fun histories(
        gameId: Long
    ): List<HttpDTO.Response.CompHistory>? {
        return extractBody(service.historyListRequest(gameId).execute())
    }

    suspend fun historyDetail(
        gameId: Long
    ): HttpDTO.Response.DetailHistory? {
        return extractBody(service.historyDetailRequest(gameId).execute())
    }

    suspend fun loginByKakao(
        code: String
    ): HttpDTO.Response.User? {
        return extractBody(service.loginByKakao(code).execute())
    }

    suspend fun adjacentRanking(
    ): HttpDTO.Response.Rank? {
        return extractBody(service.adjacentRanking().execute())
    }

    suspend fun underRanking(
        uid: Long
    ): HttpDTO.Response.Rank? {
        return extractBody(service.underRanking(uid).execute())
    }

    suspend fun overRanking(
        uid: Long
    ): HttpDTO.Response.Rank? {
        return extractBody(service.overRanking(uid).execute())
    }
    suspend fun uploadImage(
        image: MultipartBody.Part?
    ): String? {
        return extractBody(service.uploadImage(image).execute())
    }

    suspend fun delImage(
    ): String? {
        return extractBody(service.deleteImage().execute())
    }

    suspend fun getImage(
        uid: Long
    ): String? {
        return extractBody(service.getImage(uid).execute())
    }

    private fun <T> extractBody(response: Response<T>): T? {
        val header = response.headers()
        val code = response.code()
        val message = response.message()
        val body = response.body()

        Log.d(TAG, "code : $code" +
                "\nheader : $header" +
                "\nbody : $body" +
                "\nmessage : $message" +
                "\nerror body: ${response.errorBody().toString()}" +
                "\nraw: ${response.raw()}"
        )

        return body
    }

    /**
     * @param ifFail Exception 발생시 실행될 함수
     * @param block execute 할 함수들을 넣는 block
     * @return Job
     *
     * @sample execute
     *
     * @see Job
     */
    fun execute(ifFail: suspend () -> Unit = {}, block: suspend HttpSyncService.() -> Unit): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "execute block start")
                block()
                Log.d(TAG, "execute block end")
            } catch (e : Exception) {
                Log.e(TAG, "execute exception\n${e.message}")
                ifFail()
            }
        }
    }
}