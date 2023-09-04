package com.example.quoridor

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quoridor.databinding.TestProfileBinding

class ProfileTest:AppCompatActivity() {
    private val binding:TestProfileBinding by lazy{
        TestProfileBinding.inflate(layoutInflater)
    }

    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("minseok","?")
        //버튼 이벤트
        binding.buttonAlbum.setOnClickListener {
            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        binding.buttonCamera.setOnClickListener {
            //사진 촬영
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            activityResult2.launch(intent)
        }
    }//onCreate

    //결과 가져오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 OK , 결가값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            Log.d("minseok","1")
            val uri  = it.data!!.data
            Log.d("minseok","1"+uri)
            //화면에 보여주기
            Glide.with(this)
                .load(uri) //이미지
                .into(binding.imagePreview) //보여줄 위치
        }
    }

    private val activityResult2: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            Log.d("minseok","2")
            val extras = it.data?.extras
            if (extras != null) {
                val thumbnail = extras.get("data") as Bitmap?
                if (thumbnail != null) {
                    Glide.with(this)
                        .load(thumbnail)
                        .into(binding.imagePreview)
                }
            }

        }
    }

}