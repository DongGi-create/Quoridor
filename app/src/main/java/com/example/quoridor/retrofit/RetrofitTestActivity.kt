package com.example.quoridor.retrofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivityRetrofitTestBinding
import com.example.quoridor.ingame.CustomViewTestActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class RetrofitTestActivity: AppCompatActivity() {

    private val binding: ActivityRetrofitTestBinding by lazy {
        ActivityRetrofitTestBinding.inflate(layoutInflater)
    }

    private val service = Service()
    private lateinit var keepTryingJob: Deferred<DTO.MatchingResponse?>

    private val TAG: String by lazy {
        getString(R.string.Dirtfy_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val id = binding.idEt.text.toString()
            val pw = binding.pwEt.text.toString()

            service.login(id, pw, object : HttpResult<DTO.SignUpResponse> {
                override fun success(data: DTO.SignUpResponse) {
                    popToast("success!")
                }

                override fun appFail() {
                    popToast("app fail")
                }


                override fun fail(throwable: Throwable) {
                    popToast("fail")
                }

                override fun finally() {

                }
            })
        }

//        binding.button.setOnClickListener{
//            val job = CoroutineScope(Dispatchers.IO).async {
//                var gameId :String?
//                var matchingResponse: DTO.MatchingResponse? = null
//                gameId = null
//                while(gameId == null) {
//                    service.match(2, 1, object : HttpResult<DTO.MatchingResponse> {
//                        override fun success(data: DTO.MatchingResponse) {
//                            popToast("success! data: $data")
//                            gameId = data.gameId
//                            matchingResponse = data
//                        }
//
//                        override fun appFail() {
//                            popToast("app fail")
//                        }
//
//                        override fun fail(throwable: Throwable) {
//                            popToast("fail")
//                        }
//                    })
//                    delay(1000)
//                }
//                matchingResponse
//            }
//            CoroutineScope(Dispatchers.Main).launch {
//                popToast("매칭성공! GameID: " + job.await().toString())//await는 비동기로만 받을 수 있다
//                val intent = Intent(this@RetrofitTestActivity, CustomViewTestActivity::class.java)
//                startActivity(intent)
//            }
//        }


        binding.button.setOnClickListener {
            if (!this@RetrofitTestActivity::keepTryingJob.isInitialized || !keepTryingJob.isActive) {
                keepTryingJob = buildKeepTryingJob()
                buildGameStartJob(keepTryingJob).start()
                keepTryingJob.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (this@RetrofitTestActivity::keepTryingJob.isInitialized && keepTryingJob.isActive) {
            keepTryingJob.cancel()
        }
    }

    private suspend fun buildAwaitingJob(): Job {
        return CoroutineScope(Dispatchers.Default)
                .launch(start = CoroutineStart.LAZY) {
                    Log.d(TAG, "awaitingJob start")

                    var timeout = 15
                    while (timeout-- > 0){
                        Log.d(TAG, "awaitingJob running $timeout")
                        delay(1000)
                    }

                    Log.d(TAG, "awaitingJob end")
                }
    }
    private suspend fun buildMatchRequestingJob(): Deferred<DTO.MatchingResponse?> {
        val awaitingJob = buildAwaitingJob()
        return CoroutineScope(Dispatchers.IO)
            .async(start = CoroutineStart.LAZY) {

                Log.d(TAG, "matchRequestingJob start")
                var matchingResponse: DTO.MatchingResponse? = null

                awaitingJob.start()

                service.match(2, 1, object : HttpResult<DTO.MatchingResponse> {
                    override fun success(data: DTO.MatchingResponse) {
                        popToast("success! data: $data")
                        matchingResponse = data

                        Log.d(TAG, "matchRequestingJob get response success")
                    }

                    override fun appFail() {
                        popToast("app fail")

                        Log.d(TAG, "matchRequestingJob get response app fail")
                    }

                    override fun fail(throwable: Throwable) {
                        popToast("fail")

                        Log.d(TAG, "matchRequestingJob get response fail")
                    }

                    override fun finally() {
                        awaitingJob.cancel()
                    }
                })

                try {
                    supervisorScope {
                        awaitingJob.join()
                    }
                    Log.d(TAG, "matchRequestingJob end")
                    matchingResponse
                } catch (e : CancellationException) {
                    Log.d(TAG, "matchRequestingJob end")
                    matchingResponse
                }
            }
    }
    private fun buildKeepTryingJob(): Deferred<DTO.MatchingResponse?> {
        return CoroutineScope(Dispatchers.Default)
            .async(start = CoroutineStart.LAZY) {
                Log.d(TAG, "keepTryingJob start")
                var response: DTO.MatchingResponse? = null

                var count = 30
                while (count-- > 0){
                    Log.d(TAG, "keepTryingJob running $count")
                    val matchRequestingJob = buildMatchRequestingJob()
                    matchRequestingJob.start()
                    response = matchRequestingJob.await()
                    if (response != null) break
                    delay(1000)
                }

                Log.d(TAG, "keepTryingJob end")
                response
            }
    }
    private fun buildGameStartJob(keepTryingJob: Deferred<DTO.MatchingResponse?>): Job {
        return CoroutineScope(Dispatchers.Main)
            .launch(start = CoroutineStart.LAZY) {
                Log.d(TAG, "gameStartJob start")
                val gameId = keepTryingJob.await()
                Log.d(TAG, "gameStartJob awaited $gameId")
                Log.d(TAG, "gameStartJob end")

                if (gameId == null) {
                    popToast("매칭 실패")
                }
                else {
                    popToast("매칭성공! GameID: $gameId")//await는 비동기로만 받을 수 있다
                    val intent = Intent(this@RetrofitTestActivity, CustomViewTestActivity::class.java)
                    //startActivity(intent)
                }
            }
    }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}