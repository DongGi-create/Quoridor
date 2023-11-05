package com.example.quoridor

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.databinding.ActivitySignupBinding
import com.example.quoridor.dialog.CustomDialogInterface
import com.example.quoridor.dialog.EditProfileImageDialog
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.UserManager
import com.example.quoridor.util.Func.getAny

class SignUpActivity :AppCompatActivity(){
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val httpService = HttpService()

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var resultHandler: ActivityResultHandler
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
        resultHandler = ActivityResultHandler(this,binding.signupIvProfile)

        binding.signupIvProfile.setOnClickListener{
            Log.d("minseok","CLICKED")
            val dialog = EditProfileImageDialog(this)

            dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
            dialog.setClickListener(object: CustomDialogInterface {
                override fun onCameraClicked() {
                    Log.d(TAG,"camera")
                    resultHandler.launchBitmapActivity(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    dialog.dismiss()
                }
                override fun onAlbumClicked() {
                    Log.d(TAG,"album")
                    val its = Intent(Intent.ACTION_PICK)
                    its.type = "image/*"
                    resultHandler.launchUriActivity(its)
                    dialog.dismiss()
                }
                override fun onDelPhotoClicked() {
                    Log.d(TAG,"del photo")
                    binding.signupIvProfile.setImageResource(R.drawable.ic_identity)
                    resultHandler.filePath = null
                    dialog.dismiss()
                }
            })
            dialog.show()
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

            HttpSyncService.execute {
                signUp(id,pw,email,name)
                Log.d("minseok","a")
                UserManager.setUser(login(id, pw)!!)
                resultHandler.editProfile(null)
                val intent = Intent(this@SignUpActivity,MainActivity::class.java)
                startActivity(intent)
            }
            /*httpService.signUp(id,pw,email,name, object: HttpResult<HttpDTO.Response.User> {
                override fun success(data: HttpDTO.Response.User) {
                    Log.d(TAG,"SignUp success!")
                }

                override fun appFail() {
                    Log.d(TAG,"app fail")
                }

                override fun fail(throwable: Throwable) {
                    Log.d(TAG,"fail")
                }
                override fun finally() {
                }
            })

            httpService.login(id, pw, ToastHttpResult(applicationContext, "login", TAG))*/

            /*resultHandler.editProfile(null)*/
            

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,LoginActivity::class.java))
    }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}