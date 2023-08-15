package com.example.quoridor.retrofit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivitySignupTestBinding

class SignUpTestActivity:AppCompatActivity() {
    private val binding: ActivitySignupTestBinding by lazy {
        ActivitySignupTestBinding.inflate(layoutInflater)
    }

    private val service = Service()

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener{
            val id = binding.idEt.text.toString()
            val pw = binding.pwEt.text.toString()
            val email = binding.idEmail.text.toString()
            val name = binding.idName.text.toString()

            service.signUp(id,pw,email,name, object: HttpResult<DTO.SignUpResponse>{
                override fun success(data: DTO.SignUpResponse) {
                    popToast("SignUp success!")
                    val intent = Intent(this@SignUpTestActivity, LoginActivity::class.java)
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