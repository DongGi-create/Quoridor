package com.example.quoridor.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.loginBtnLogin.setOnClickListener{
            val id = binding.loginEtId.text.toString()
            val pw = binding.loginEtPw.text.toString()

            httpService.login(id,pw, object: HttpResult<HttpDTO.Response.User> {
                override fun success(data: HttpDTO.Response.User) {
                    popToast("success!")
                    /*val user = UserManager.getInstance()*/
                    val user = UserManager
                    user.setUser(data, false)
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    this@LoginActivity.finish()
                }

                override fun appFail() {
                    popToast("app fail")
                }

                override fun fail(throwable: Throwable) {
                    popToast("fail")
                }

                override fun finally() {

                }
            }){
                Log.d("oz","Login Fail status Code ${404}")
                if(it == 404){
                    popToast("로그인 정보가 달라요..")
                }
                else{
                    popToast("app fail")
                }
            }
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


        binding.loginTvForgetPw.setOnClickListener{
            val dialog = needDialog(R.layout.dialog_forget_pw)
            val temporyPwDialog = needDialog(R.layout.dialog_game_quit)
            val yesBtn = dialog.findViewById<Button>(R.id.forget_pw_ok_btn)
            yesBtn.setOnClickListener {
                val text = dialog.findViewById<EditText>(R.id.forget_pw_get_id).text.toString()
                if(text.isBlank()){
                    Toast.makeText(this@LoginActivity, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else{
                    httpService.getNewPw(text, object:HttpResult<HttpDTO.Response.NewPw>{
                        override fun success(data: HttpDTO.Response.NewPw) {
                            Log.d("oz","new password : $data.newPassword")
                            dialog.dismiss()
                            temporyPwDialog.findViewById<TextView>(R.id.quit_text).text = data.newPassword
                            temporyPwDialog.findViewById<Button>(R.id.yes_btn).setOnClickListener {
                                temporyPwDialog.dismiss()
                            }
                            val paste:Button = temporyPwDialog.findViewById<Button>(R.id.no_btn)
                            paste.text = "Paste"
                            paste.setOnClickListener{
                                val clipboard: ClipboardManager = this@LoginActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Password", data.newPassword)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(this@LoginActivity, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                            temporyPwDialog.show()
                        }

                        override fun appFail() {
                            Log.d("oz","forget pw dialog appFail")
                        }

                        override fun fail(throwable: Throwable) {
                            Log.d("oz","forget pw dialog Fail")
                        }

                        override fun finally() {
                            Log.d("oz","forget pw dialog finally")
                        }
                    }){
                        Log.d("oz","status code $it")
                        if(it == 404){
                            Toast.makeText(this@LoginActivity, "없는 아이디입니다.",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@LoginActivity, "app fail",Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
            dialog.show()
        }

        //통신(로그인 통신하는동안 아무 액션도 안먹히게 해야 할듯)
        binding.loginTvRegister.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        binding.kakaoLoginImageView.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
            dialog.setContentView(R.layout.dialog_coming_soon)
            val text = dialog.findViewById<TextView>(R.id.dcs_dialog_text)
            text.text = "(선택)이메일 동의를\n반드시 체크해주셔야 합니다.!"
            text.gravity = Gravity.CENTER
            text.textSize = 20f
            val yesBtn = dialog.findViewById<Button>(R.id.ok_btn)
            yesBtn.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, KakaoLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            dialog.show()
        }
    }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }

    private fun needDialog(layout: Int):Dialog{
        val tempdialog = Dialog(this)
        tempdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        tempdialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
        tempdialog.setContentView(layout)
        return tempdialog
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