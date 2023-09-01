package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.databinding.ActivitySignupBinding
import com.example.quoridor.login.LoginActivity

class SignUpActivity :AppCompatActivity(){
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val httpService = HttpService()

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signupIvProfile.setOnClickListener{

        }

        binding.signupBtnSignup.setOnClickListener{
            val id = binding.signupEtId.text.toString()
            val pw = binding.signupEtPw.text.toString()
            val email = binding.signupEtEmail.text.toString()
            val name = binding.signupEtName.text.toString()

            httpService.signUp(id,pw,email,name, object: HttpResult<HttpDTO.Response.User> {
                override fun success(data: HttpDTO.Response.User) {
                    popToast("SignUp success!")
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
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
    }
    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}