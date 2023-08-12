package com.example.quoridor

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityLoginBinding
import com.example.quoridor.databinding.ActivitySignupTestBinding

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignupTestBinding by lazy {
        ActivitySignupTestBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}