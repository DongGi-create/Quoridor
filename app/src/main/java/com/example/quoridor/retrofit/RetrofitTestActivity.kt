package com.example.quoridor.retrofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.GameForLocalActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivityRetrofitTestBinding
import com.example.quoridor.retrofit.util.RetrofitFunc
import com.example.quoridor.retrofit.util.ToastHttpResult
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

            service.login(id, pw, ToastHttpResult(applicationContext, "login", TAG))
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
                val matchData = DTO.MatchingRequest(2, 1)

                keepTryingJob = RetrofitFunc.buildKeepTryingJob(
                    matchData,
                    service::makeMatchCall,
                    ToastHttpResult(applicationContext, "matching", TAG))

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

    private fun buildGameStartJob(keepTryingJob: Deferred<DTO.MatchingResponse?>): Job {
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