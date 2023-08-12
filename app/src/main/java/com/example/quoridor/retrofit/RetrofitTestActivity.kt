package com.example.quoridor.retrofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivityRetrofitTestBinding
import com.example.quoridor.ingame.CustomViewTestActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RetrofitTestActivity: AppCompatActivity() {

    private val binding: ActivityRetrofitTestBinding by lazy {
        ActivityRetrofitTestBinding.inflate(layoutInflater)
    }

    private val service = Service()

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
            })
        }

        binding.button.setOnClickListener{
            val job = CoroutineScope(Dispatchers.IO).async {
                var gameId :String?
                var matchingResponse: DTO.MatchingResponse? = null
                gameId = null
                while(gameId == null) {
                    service.match(2, 1, object : HttpResult<DTO.MatchingResponse> {
                        override fun success(data: DTO.MatchingResponse) {
                            popToast("success! data: $data")
                            gameId = data.gameId
                            matchingResponse = data
                        }

                        override fun appFail() {
                            popToast("app fail")
                        }

                        override fun fail(throwable: Throwable) {
                            popToast("fail")
                        }
                    })
                    delay(1000)
                }
                matchingResponse
            }
            CoroutineScope(Dispatchers.Main).launch {
                popToast("매칭성공! GameID: " + job.await().toString())//await는 비동기로만 받을 수 있다
                val intent = Intent(this@RetrofitTestActivity, CustomViewTestActivity::class.java)
                startActivity(intent)
            }
        }
    }




    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}