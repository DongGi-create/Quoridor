package com.example.quoridor

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityMypageBinding

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
    }
}