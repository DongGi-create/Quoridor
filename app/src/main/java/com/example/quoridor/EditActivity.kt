package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.communication.socket.WebSocketTest
import com.example.quoridor.databinding.ActivityEditBinding
import com.example.quoridor.dialog.CustomDialogInterface
import com.example.quoridor.dialog.EditProfileImageDialog
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class EditActivity:AppCompatActivity() {
    private val binding:ActivityEditBinding by lazy{
        ActivityEditBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy{
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var resultHandler: ActivityResultHandler

    private val service = HttpService()
    private var id:String? = null
    private var pw:String? = null
    private var email:String? = null
    private var name:String? = null
    private var profileLink:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        id = UserManager.umid
        pw = UserManager.umpw
        email = UserManager.umemail
        name = UserManager.umname
        val intent = intent
        profileLink = intent.getStringExtra("profileLinkKey")

        resultHandler = ActivityResultHandler(this,binding.editIvProfile)

        binding.editIconCamera.setOnClickListener{
            Log.d("minseok","CLICKED")
            val dialog = EditProfileImageDialog(this)

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
                    binding.editIvProfile.setImageResource(R.drawable.ic_identity)
                    resultHandler.filePath = null
                    dialog.dismiss()
                }
            })
            dialog.show()
        }
        Glide.with(this@EditActivity)
            .load(profileLink).into(binding.editIvProfile)
        binding.editIvProfile
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

            HttpSyncService.execute {
                userUpdate(pw, email, name)
                resultHandler.editProfile(profileLink)
            }
            /*service.userUpdate(pw, email, name, object:HttpResult<HttpDTO.Response.User>{
                override fun success(data: HttpDTO.Response.User) {
                    Log.d(TAG,"Edit success!")
                    UserManager.setUser(data)

                }
                override fun appFail() {
                    Log.d(TAG,"app fail")
                }
                override fun fail(throwable: Throwable) {
                    Log.d(TAG,"fail")
                }
                override fun finally() {
                }
            })*/

            //userupdate랑 프로필 수정은 동시에 가능
            val mainActivityIt = Intent(this@EditActivity,MainActivity::class.java)
            startActivity(mainActivityIt)
        }

    }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}