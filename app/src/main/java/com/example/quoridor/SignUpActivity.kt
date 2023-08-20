package com.example.quoridor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.quoridor.databinding.ActivitySignupBinding
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.retrofit.DTO
import com.example.quoridor.retrofit.HttpResult
import com.example.quoridor.retrofit.Service

class SignUpActivity :AppCompatActivity(){
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val service = Service()

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

            service.signUp(id,pw,email,name, object: HttpResult<DTO.SignUpResponse> {
                override fun success(data: DTO.SignUpResponse) {
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