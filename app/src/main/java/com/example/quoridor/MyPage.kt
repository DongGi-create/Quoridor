package com.example.quoridor

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityMypageBinding
import com.example.quoridor.login.UserManager

class MyPage:AppCompatActivity() {
    val binding:ActivityMypageBinding by lazy{
        ActivityMypageBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val user = UserManager.getInstance() // UserManager 클래스에 맞게 수정
        binding.tvMyPageName.setText(user.getName())
        binding.btnMyPageLogout.setOnClickListener{
            user.setId("")
            val intent = Intent(this@MyPage,MainActivity::class.java)
            startActivity(intent)
            //세션 끊기 넣기
        }
    }
}