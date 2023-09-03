package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.databinding.ActivitySignupBinding
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.util.Func.getAny
import com.example.quoridor.util.Func.getUser
import com.google.api.Http
import okhttp3.MediaType.Companion.toMediaType

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

        val preSet = intent.getAny("user", HttpDTO.Response.User::class.java)
        if (preSet != null) {
            binding.signupEtEmail.apply {
                setText(preSet.email)
                isEnabled = false
            }
        }

        binding.signupIvProfile.setOnClickListener{

        }

        binding.signupEtPw.transformationMethod = PasswordTransformationMethod()
        (binding.signupIvSee as View).setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.action == MotionEvent.ACTION_DOWN){
                    binding.signupEtPw.transformationMethod = null
                }
                else if(event?.action==MotionEvent.ACTION_UP){
                    binding.signupEtPw.transformationMethod = PasswordTransformationMethod()
                }
                return true
            }
        })

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