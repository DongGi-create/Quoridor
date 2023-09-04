package com.example.quoridor

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.databinding.TestProfileBinding
import com.example.quoridor.login.UserManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink

class ProfileTest:AppCompatActivity() {
    private val binding:TestProfileBinding by lazy{
        TestProfileBinding.inflate(layoutInflater)
    }

    private var filePath: MultipartBody.Part? = null
    private var firstFile: String? = null

    private val service = HttpService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(UserManager.umid!=""){
            Log.d("minseok",UserManager.umuid.toString())
            service.getImage(UserManager.umuid!!,object:HttpResult<String>{
                override fun success(data: String) {
                    popToast("getImage success!")
                    if(data!="null"){
                        firstFile = data
                    }
                }
                override fun appFail() {
                    popToast("app fail")
                }
                override fun fail(throwable: Throwable) {
                    Log.d("minseok","fail "+throwable.message)
                }
                override fun finally() {
                }
            })
        }

        Log.d("minseok","en")
        //버튼 이벤트
        binding.buttonAlbum.setOnClickListener {
            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityUriResult.launch(intent)//uri
        }

        binding.buttonCamera.setOnClickListener {
            //사진 촬영
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityBitmapResult.launch(intent)
        }

        binding.testBtnUpload.setOnClickListener{
            // imagePart를 서버로 업로드
            if (filePath != null) {
                uploadImageToServer(filePath)
            } else {
                Toast.makeText(this, "이미지를 선택하세요.", Toast.LENGTH_SHORT).show()
                Log.d("minseok","no file")
                if (firstFile!=null) {
                    // deleteImage
                    service.delImage(object: HttpResult<String>{
                        override fun success(data: String) {
                            popToast("deleteImage success!")
                            firstFile = null
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
        }
    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }

    private fun bmpToMultipart(bitmap: Bitmap?){
        val bitmapRequestBody = bitmap?.let { BitmapRequestBody(it) }
        val bitmapMultipartBody: MultipartBody.Part? =
            if (bitmapRequestBody == null) null
            else MultipartBody.Part.createFormData("file", "DDYM.jpeg", bitmapRequestBody)
        filePath = bitmapMultipartBody
        Log.d("minseok",filePath.toString()+" bmpToMultipart")
    }

    private fun uriToMultipart(path: Uri?){
        //이미지 처리
        val imagePart: MultipartBody.Part? = path?.let { uri ->
            val contentResolver = contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val requestBody = inputStream?.readBytes()?.toRequestBody("image/jpeg".toMediaTypeOrNull())
            inputStream?.close() // 스트림 닫기
            MultipartBody.Part.createFormData("file", "DDYM.jpeg", requestBody!!)
        }
        filePath = imagePart
        Log.d("minseok",filePath.toString()+" uriToMultipart")
    }

    private fun uploadImageToServer(imagePart: MultipartBody.Part?){
        Log.d("minseok","upload "+imagePart.toString())
        service.uploadImage(imagePart,object: HttpResult<String>{
            override fun success(data: String) {
                Log.d("minseok",data)
            }
            override fun appFail() {
                Log.d("minseok","app fail")
            }
            override fun fail(throwable: Throwable) {
                Log.d("minseok","fail")
            }
            override fun finally() {
            }
        })
    }

    //결과 가져오기
    private val activityUriResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        //결과 코드 OK , 결과값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            val uri  = it.data!!.data
            uriToMultipart(uri)
            //화면에 보여주기
            Glide.with(this)
                .load(uri) //이미지
                .into(binding.imagePreview) //보여줄 위치
        }
    }

    private val activityBitmapResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            val extras = it.data?.extras

            if (extras != null) {
                val thumbnail = extras.get("data") as Bitmap?
                bmpToMultipart(thumbnail)
                if (thumbnail != null) {
                    Glide.with(this)
                        .load(thumbnail)
                        .into(binding.imagePreview)
                }
            }
        }
    }

    private fun popToast(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }
}