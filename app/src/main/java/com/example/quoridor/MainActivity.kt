package com.example.quoridor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.ingame.CustomViewTestActivity
import com.example.quoridor.retrofit.RetrofitTestActivity
import com.example.quoridor.retrofit.SignUpTestActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.btnGamestart.setOnClickListener {
//            val intent = Intent(this, Game1v1::class.java)
//            startActivity(intent)
//        }
//
//        binding.btnAuthtest.setOnClickListener {
//            val intent = Intent(this, test::class.java)
//            startActivity(intent)
//        }

        binding.viewTestBtn.setOnClickListener {
            val intent = Intent(this, CustomViewTestActivity::class.java)
            startActivity(intent)
        }
        binding.mainSignup.setOnClickListener {
            val intent = Intent(this, SignUpTestActivity::class.java)
            startActivity(intent)
        }
        binding.retrofitTestBtn.setOnClickListener {
            val intent = Intent(this, RetrofitTestActivity::class.java)
            startActivity(intent)
        }

        binding.loginPageBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}