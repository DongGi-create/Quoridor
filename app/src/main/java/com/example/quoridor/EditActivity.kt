package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.databinding.ActivityEditBinding
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class EditActivity:AppCompatActivity() {
    private val binding:ActivityEditBinding by lazy{
        ActivityEditBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy{
        getString(R.string.Minseok_test_tag)
    }

    private val service = HttpService()
    private var id:String? = null
    private var pw:String? = null
    private var email:String? = null
    private var name:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        id = UserManager.umid
        pw = UserManager.umpw
        email = UserManager.umemail
        name = UserManager.umname
        binding.editEtName.setText(name)
        binding.editTvId.text = id
        binding.editEtEmail.setText(email)
        binding.editEtPw.transformationMethod = PasswordTransformationMethod()
        (binding.editIvSee as View).setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.action == MotionEvent.ACTION_DOWN){
                    binding.editEtPw.transformationMethod = null
                }
                else if(event?.action == MotionEvent.ACTION_UP){
                    binding.editEtPw.transformationMethod = PasswordTransformationMethod()
                }
                return true
            }
        })

        binding.editBtnSignup.setOnClickListener{
            pw = if (binding.editEtPw.text.toString() == pw) null else binding.editEtPw.text.toString()
            email = if (binding.editEtEmail.text.toString() == email) null else binding.editEtEmail.text.toString()
            name = if (binding.editEtName.text.toString() == name) null else binding.editEtName.text.toString()

            service.userUpdate(pw, email, name, object:HttpResult<HttpDTO.Response.User>{
                override fun success(data: HttpDTO.Response.User) {
                    popToast("Edit success!")
                    UserManager.setUser(data)
                    val intent = Intent(this@EditActivity, MyPageActivity::class.java)
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