package com.example.quoridor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.HttpSyncService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink

class ActivityResultHandler(private val activity: AppCompatActivity, private val v:ImageView) {
    var filePath: MultipartBody.Part? = null

    private val service = HttpService()

    private val activityUriResult: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val uri = result.data!!.data
                uriToMultipart(uri)
                Glide.with(activity)
                    .load(uri)
                    .into(v)
            }
        }

    private val activityBitmapResult: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val extras = result.data?.extras
                if (extras != null) {
                    val thumbnail = extras.get("data") as Bitmap?
                    bmpToMultipart(thumbnail)
                    if (thumbnail != null) {
                        Glide.with(activity)
                            .load(thumbnail)
                            .into(v)
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
            val contentResolver = activity.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val requestBody = inputStream?.readBytes()?.toRequestBody("image/jpeg".toMediaTypeOrNull())
            inputStream?.close() // 스트림 닫기
            MultipartBody.Part.createFormData("file", "DDYM.jpeg", requestBody!!)
        }
        filePath = imagePart
        Log.d("minseok",filePath.toString()+" uriToMultipart")
    }


    fun launchUriActivity(intent: Intent) {
        Log.d("minseok","handler launch")
        activityUriResult.launch(intent)
    }

    fun launchBitmapActivity(intent: Intent) {
        Log.d("minseok","handler launch")
        activityBitmapResult.launch(intent)
    }

    fun uploadImageToServer(imagePart: MultipartBody.Part?){
        Log.d("minseok","upload "+imagePart.toString())
        HttpSyncService.execute {
            uploadImage(imagePart)
        }
        /*service.uploadImage(imagePart,object: HttpResult<String> {
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
        })*/
    }

    fun editProfile(profileLink: String?){
        if (filePath != null) {
            uploadImageToServer(filePath)
        } else {
            Log.d("minseok","no file")
            if (profileLink!=null) {
                // deleteImage
                HttpSyncService.execute {
                    delImage()
                }
                /*service.delImage(object: HttpResult<String>{
                    override fun success(data: String) {
                        Log.d("minseok","deleteImage success!")
                    }
                    override fun appFail() {
                        Log.d("minseok","app fail")
                    }
                    override fun fail(throwable: Throwable) {
                        Log.d("minseok","fail")
                    }
                    override fun finally() {
                    }
                })*/
            }
        }
    }
}
