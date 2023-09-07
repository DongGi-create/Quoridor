package com.example.quoridor.communication.retrofit.util

import android.util.Log
import com.example.quoridor.communication.retrofit.HttpResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RetrofitFunc {

    private val TAG = "Drirtfy Test"

    private suspend fun buildAwaitingJob(time: Int): Job {
        return CoroutineScope(Dispatchers.Default)
            .launch(start = CoroutineStart.LAZY) {
                Log.d(TAG, "awaitingJob start")

                var timeout = time
                while (timeout-- > 0) {
                    Log.d(TAG, "awaitingJob running $timeout")
                    delay(5000)
                }

                Log.d(TAG, "awaitingJob end")
            }
    }

    private suspend fun <ResponseType> buildRequestingJob(
        call: Call<ResponseType>,
        httpResult: HttpResult<ResponseType>,
        requestTimeout: Int
    ): Deferred<ResponseType?> {
        val awaitingJob = buildAwaitingJob(requestTimeout)
        return CoroutineScope(Dispatchers.IO)
            .async(start = CoroutineStart.LAZY) {

                Log.d(TAG, "requestingJob start")
                var asyncResponse: ResponseType? = null

                awaitingJob.start()

                call.enqueue(object : Callback<ResponseType> {
                    override fun onResponse(call: Call<ResponseType>, response: Response<ResponseType>) {
                        if (response.isSuccessful) {
                            val result = response.body()

                            if (result != null) {
                                httpResult.success(result)
                                asyncResponse = result
                            } else {
                                httpResult.appFail()
                            }
                        } else {
                            httpResult.appFail()
                        }
                        httpResult.finally()

                        awaitingJob.cancel()
                    }

                    override fun onFailure(call: Call<ResponseType>, t: Throwable) {
                        httpResult.fail(t)
                        httpResult.finally()
                        awaitingJob.cancel()
                    }
                })

                try {
                    supervisorScope {
                        awaitingJob.join()
                    }
                    Log.d(TAG, "requestingJob end")
                    asyncResponse
                } catch (e: CancellationException) {
                    Log.d(TAG, "requestingJob end")
                    asyncResponse
                }
            }
    }

    fun <RequestType, ResponseType> buildKeepTryingJob(
        data: RequestType,
        callMaker: (RequestType) -> Call<ResponseType>,
        httpResult: HttpResult<ResponseType>,
        validTest: (ResponseType) -> Boolean,
        delay: Long = 5000L,
        requestTimeout: Int = 15
    ): Deferred<ResponseType?> {
        return CoroutineScope(Dispatchers.Default)
            .async(start = CoroutineStart.LAZY) {
                Log.d(TAG, "keepTryingJob start")
                var response: ResponseType? = null

                var count = 30
                while (count-- > 0) {
                    Log.d(TAG, "keepTryingJob running $count")
                    val requestingJob = buildRequestingJob(callMaker(data), httpResult, requestTimeout)
                    requestingJob.start()
                    response = requestingJob.await()
                    if (response != null && validTest(response)) break
                    delay(delay)
                }

                Log.d(TAG, "keepTryingJob end")
                response
            }
    }

    /**
     * @param times 반복할 횟수
     * @param delay task 실행 후 delay
     * @param earlyStop 중간에 멈출지 확인하는 함수
     * @param task 수행할 일
     * @return Deferred<ReturnType>
     *
     * @sample buildRepeatJob
     *
     * @see Deferred
     */
    fun <ReturnType> buildRepeatJob(
        times: Int = 30,
        delay: Long = 5000L,
        earlyStop: suspend (ReturnType?) -> Boolean = { false },
        task: suspend (ReturnType?) -> ReturnType
    ): Deferred<ReturnType?> {
        return CoroutineScope(Dispatchers.Default)
            .async(start = CoroutineStart.LAZY) {
                Log.d(TAG, "KeepDoingJob start")

                var count = times
                var returnValue: ReturnType? = null
                while (count-- == 0) {
                    Log.d(TAG, "KeepDoingJob running $count")
                    returnValue = task(returnValue)
                    if (earlyStop(returnValue)) break
                    delay(delay)
                }

                Log.d(TAG, "KeepDoingJob end")

                returnValue
            }
    }

}