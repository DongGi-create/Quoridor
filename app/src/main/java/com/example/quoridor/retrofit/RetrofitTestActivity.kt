package com.example.quoridor.retrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivityRetrofitTestBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitTestActivity: AppCompatActivity() {

    private val binding: ActivityRetrofitTestBinding by lazy {
        ActivityRetrofitTestBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy {
        getString(R.string.Dirtfy_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder().baseUrl("http://43.201.189.249:8080/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(LoginService::class.java)

        binding.loginBtn.setOnClickListener {
            val id = binding.idEt.text.toString()
            val pw = binding.pwEt.text.toString()

            val body = LoginDTO(id, pw)

            service.login(body).enqueue(object: Callback<LoginDTO>{
                override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                    if (response.isSuccessful){
                        val result = response.body()

                        Log.d(TAG, "response successful " + result?.toString())
                        popToast("successful!")
                    }
                    else {
                        Log.d(TAG, "response fail")
                        popToast("fail?")
                    }
                }

                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                    Log.d(TAG, "onFail " + t.message.toString())
                    popToast("onFailure")
                }
            })
        }
    }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}