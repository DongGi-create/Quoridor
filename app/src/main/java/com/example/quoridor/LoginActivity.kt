package com.example.quoridor

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityLoginBinding
import com.example.quoridor.databinding.ActivityRetrofitTestBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        var fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in)
        var bottom_down = AnimationUtils.loadAnimation(this,R.anim.bottom_down)


        binding.linearLoginTop.animation = bottom_down

        var handler = Handler()
        var runnable = Runnable{
            binding.cardView.animation = fade_in
            binding.cvLoginIcon.animation = fade_in
            binding.tvLoginTitle.animation = fade_in
            binding.tvLoginRegister.animation=fade_in
            binding.linearLoginRegister.animation=fade_in
        }

        handler.postDelayed(runnable,1000)
    }

}