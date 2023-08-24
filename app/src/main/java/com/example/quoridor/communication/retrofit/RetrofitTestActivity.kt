package com.example.quoridor.communication.retrofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.GameForLocalActivity
import com.example.quoridor.R
import com.example.quoridor.communication.retrofit.util.RetrofitFunc
import com.example.quoridor.communication.retrofit.util.ToastHttpResult
import com.example.quoridor.databinding.ActivityRetrofitTestBinding
import com.example.quoridor.util.Func.popToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RetrofitTestActivity: AppCompatActivity() {

    private val binding: ActivityRetrofitTestBinding by lazy {
        ActivityRetrofitTestBinding.inflate(layoutInflater)
    }

    private val service = Service()
    private lateinit var keepTryingJob: Deferred<HttpDTO.MatchingResponse?>
    private lateinit var gameStartJob: Job

    private val TAG: String by lazy {
        getString(R.string.Dirtfy_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val id = binding.idEt.text.toString()
            val pw = binding.pwEt.text.toString()

            service.login(id, pw, ToastHttpResult(applicationContext, "login", TAG))
        }

//        binding.button.setOnClickListener{
//            val job = CoroutineScope(Dispatchers.IO).async {
//                var gameId :String?
//                var matchingResponse: DTO.MatchingResponse? = null
//                gameId = null
//                while(gameId == null) {
//                    service.match(1, object : HttpResult<DTO.MatchingResponse> {
//                        override fun success(data: DTO.MatchingResponse) {
//                            popToast(this@RetrofitTestActivity, "success! data: $data")
//                            gameId = data.gameId
//                            matchingResponse = data
//                        }
//
//                        override fun appFail() {
//                            popToast(this@RetrofitTestActivity, "app fail")
//                        }
//
//                        override fun fail(throwable: Throwable) {
//                            popToast(this@RetrofitTestActivity, "fail")
//                        }
//
//                        override fun finally() {
//                            popToast(this@RetrofitTestActivity, "end")
//                        }
//                    })
//                    delay(5000)
//                }
//                matchingResponse
//            }
//            CoroutineScope(Dispatchers.Main).launch {
//                popToast(this@RetrofitTestActivity, "매칭성공! GameID: " + job.await().toString())//await는 비동기로만 받을 수 있다
//                val intent = Intent(this@RetrofitTestActivity, GameForPvPActivity::class.java)
//                startActivity(intent)
//            }
//        }


        binding.button.setOnClickListener {
            if (!this@RetrofitTestActivity::keepTryingJob.isInitialized || !keepTryingJob.isActive) {
                val matchData = HttpDTO.MatchingRequest(1)

                keepTryingJob = RetrofitFunc.buildKeepTryingJob(
                    matchData,
                    service::makeMatchCall,
                    ToastHttpResult(applicationContext, "matching", TAG),
                    {
                        !(it.gameId == null || it.turn == null)
                    })

                gameStartJob = buildGameStartJob(keepTryingJob)

                gameStartJob.start()
                keepTryingJob.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (this@RetrofitTestActivity::keepTryingJob.isInitialized && keepTryingJob.isActive) {
            keepTryingJob.cancel()
        }
        if (this@RetrofitTestActivity::gameStartJob.isInitialized && gameStartJob.isActive) {
            gameStartJob.cancel()
        }
    }

    private fun buildGameStartJob(keepTryingJob: Deferred<HttpDTO.MatchingResponse?>): Job {
        return CoroutineScope(Dispatchers.Main)
            .launch(start = CoroutineStart.LAZY) {
                Log.d(TAG, "gameStartJob start")
                val gameId = keepTryingJob.await()
                Log.d(TAG, "gameStartJob awaited $gameId")
                Log.d(TAG, "gameStartJob end")

                if (gameId == null) {
                    popToast(applicationContext, "매칭 실패")
                }
                else {
                    popToast(applicationContext, "매칭성공! GameID: $gameId")//await는 비동기로만 받을 수 있다
                    val intent = Intent(this@RetrofitTestActivity, GameForLocalActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}