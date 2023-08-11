package com.example.quoridor.retrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivityRetrofitTestBinding
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.CookieManager

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

            service.login(id, pw, object : HttpResult<LoginDTO> {
                override fun success(data: LoginDTO) {
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
        }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}