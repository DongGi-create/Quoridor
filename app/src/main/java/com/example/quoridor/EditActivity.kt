package com.example.quoridor

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.databinding.ActivityEditBinding
import com.example.quoridor.dialog.CustomDialogInterface
import com.example.quoridor.dialog.EditProfileImageDialog
import com.example.quoridor.login.UserManager


class EditActivity:AppCompatActivity() {
    private val binding:ActivityEditBinding by lazy{
        ActivityEditBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy{
        getString(R.string.Minseok_test_tag)
    }
    // PasswordTransformationMethod를 하나의 변수에 저장
    private val passwordTransformationMethod = PasswordTransformationMethod()

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
        Log.d(TAG, "프로필 사진$profileLink")
        resultHandler = ActivityResultHandler(this,binding.editIvProfile)

        binding.editIvProfile.clipToOutline = true
        /*if(profileLink != null) {
            resultHandler.filePath = "이미지 존재"
        }*/
        binding.editIconCamera.setOnClickListener{
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
                    binding.editIvProfile.setImageResource(R.drawable.ic_identity)
                    resultHandler.filePath = null
                    resultHandler.deleted = true
                    dialog.dismiss()
                }
            })
            dialog.show()
        }

        binding.editTvId.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 레이아웃이 그려진 후에 높이를 가져옵니다.
                val viewHeight = binding.editTvId.height
                // 필요한 높이를 기반으로 텍스트 크기를 동적으로 설정합니다.
                binding.editTvId.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewHeight * 0.4f)
                binding.editEtEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewHeight * 0.4f)
                binding.editEtPw.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewHeight * 0.4f)
                binding.editEtPwCheck.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewHeight * 0.4f)
                binding.editEtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewHeight * 0.4f)

                // 리스너를 제거합니다.
                binding.editTvId.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })



        Glide.with(this@EditActivity)
            .load(profileLink).into(binding.editIvProfile)
        binding.editIvProfile
        binding.editEtName.setText(name)
        binding.editTvId.text = id
        binding.editEtEmail.setText(email)

        if(profileLink == null){
            Glide.with(this@EditActivity)
                .load(R.drawable.ic_identity).into(binding.editIvProfile)
        }

        /*binding.editEtPw.transformationMethod = PasswordTransformationMethod()
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

        binding.editEtPwCheck.transformationMethod = PasswordTransformationMethod()

        (binding.editIvSeeCheck as View).setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.action == MotionEvent.ACTION_DOWN){
                    binding.editEtPw.transformationMethod = null
                }
                else if(event?.action == MotionEvent.ACTION_UP){
                    binding.editEtPw.transformationMethod = PasswordTransformationMethod()
                }
                return true
            }
        })*/

        // EditText 및 ImageView에 대한 터치 이벤트 처리
        setTouchPasswordVisibility(binding.editEtPw, binding.editIvSee)
        setTouchPasswordVisibility(binding.editEtPwCheck, binding.editIvSeeCheck)

        binding.editBtnSignup.setOnClickListener{
            if (binding.editEtPw.text.toString() == pw) {
                var newpw:String? = binding.editEtPwCheck.text.toString()
                if(newpw == ""){
                    popToast("비밀번호를 쳐주세요")
                }
                else{
                    newpw = if (binding.editEtPwCheck.text.toString() == pw) null else binding.editEtPwCheck.text.toString()
                    email = if (binding.editEtEmail.text.toString() == email) null else binding.editEtEmail.text.toString()
                    name = if (binding.editEtName.text.toString() == name) null else binding.editEtName.text.toString()

                    HttpSyncService.execute {
                        UserManager.setUser(userUpdate(newpw, email, name)!!)
                        Log.d(TAG,"changed$newpw")
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
                    this.finish()
                }
            }
            else{
                popToast("비밀번호가 달라요..")
            }

        }

    }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchPasswordVisibility(editText: EditText, imageView: ImageView) {
        (imageView as View).setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> editText.transformationMethod = null
                MotionEvent.ACTION_UP -> editText.transformationMethod = passwordTransformationMethod
            }
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}