package com.example.quoridor.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quoridor.MainActivity
import com.example.quoridor.R
import com.example.quoridor.SignUpActivity
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val httpService = HttpService()
    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var sharedLoginModel: SharedLoginModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedLoginModel = ViewModelProvider(this)[SharedLoginModel::class.java]
        binding.loginBtnLogin.setOnClickListener{
            val id = binding.loginEtId.text.toString()
            val pw = binding.loginEtPw.text.toString()

            httpService.login(id,pw, object: HttpResult<HttpDTO.Response.User> {
                override fun success(data: HttpDTO.Response.User) {
                    popToast("success!")
                    /*val user = UserManager.getInstance()*/
                    val user = UserManager
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

        binding.loginEtPw.transformationMethod = PasswordTransformationMethod()

        (binding.loginIvSee as View).setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    binding.loginEtPw.transformationMethod = null
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    binding.loginEtPw.transformationMethod = PasswordTransformationMethod()
                }
                return true
            }
        })

        //통신(로그인 통신하는동안 아무 액션도 안먹히게 해야 할듯)
        binding.loginTvRegister.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        binding.kakaoLoginImageView.setOnClickListener {
            val intent = Intent(this, KakaoLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}

/*var fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
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
        handler.postDelayed(runnable,1000)*/