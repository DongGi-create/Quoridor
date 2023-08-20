package com.example.quoridor.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quoridor.MainActivity
import com.example.quoridor.R
import com.example.quoridor.SignUpActivity
import com.example.quoridor.databinding.ActivityLoginBinding
import com.example.quoridor.retrofit.DTO
import com.example.quoridor.retrofit.HttpResult
import com.example.quoridor.retrofit.Service
import com.example.quoridor.retrofit.SignUpTestActivity

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val service = Service()
    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var sharedLoginModel: SharedLoginModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        var bottom_down = AnimationUtils.loadAnimation(this, R.anim.bottom_down)

        binding.linearLoginTop.animation = bottom_down

        var handler = Handler()
        var runnable = Runnable{
            binding.cvLoginBottom.animation = fade_in
            binding.cvLoginIcon.animation = fade_in
            binding.tvLoginTitle.animation = fade_in
            binding.tvLoginRegister.animation=fade_in
            binding.linearLoginRegister.animation=fade_in
        }
        handler.postDelayed(runnable,1000)

        sharedLoginModel = ViewModelProvider(this).get(SharedLoginModel::class.java)
        binding.btnLoginLogin.setOnClickListener{
            val id = binding.etLoginId.text.toString()
            val pw = binding.etLoginPassword.text.toString()

            service.login(id,pw, object: HttpResult<DTO.SignUpResponse>{
                override fun success(data: DTO.SignUpResponse) {
                    popToast("success!")
                    val user = UserManager.getInstance()
                    user.setUser(data)
                    sharedLoginModel.setLoginSuccess(true)
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
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
        //통신(로그인 통신하는동안 아무 액션도 안먹히게 해야 할듯)
        binding.tvLoginRegister.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.lottieLoginArrow.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}