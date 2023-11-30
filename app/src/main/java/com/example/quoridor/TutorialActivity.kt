package com.example.quoridor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quoridor.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityTutorialBinding.inflate(layoutInflater)
    }

    private val textList = listOf(
        "기본적으로 4방향으로 움직일 수 있습니다",
        "시작한 위치 반대편에 도달하면 승리합니다",
        "앞에 상대방이 있다면 다른 방향으로 움직일 수도 있습니다",
        "벽을 두어서 막을 수 있습니다",
        "이때도 마찬가지로 벽으로 막을 수 있습니다",
        "그렇지만 한 명이라도 목적지에 도달할 수 있는 경우의 수가 없다면 벽을 둘 수 없습니다",
        "따라서 가두는 것도 불가능 합니다"
    )
    private val imageList = listOf(
        R.drawable.tutorial_0,
        R.drawable.tutorial_1,
        R.drawable.tutorial_2,
        R.drawable.tutorial_3,
        R.drawable.tutorial_4,
        R.drawable.tutorial_5,
        R.drawable.tutorial_6
    )
    private var idx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textView.text = textList[idx]
        binding.imageView.setImageResource(imageList[idx])

        binding.prevButton.setOnClickListener {
            if (idx > 0)
                idx--
            binding.textView.text = textList[idx]
            binding.imageView.setImageResource(imageList[idx])
        }

        binding.nextButton.setOnClickListener {
            if (idx < 6)
                idx++
            binding.textView.text = textList[idx]
            binding.imageView.setImageResource(imageList[idx])
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}