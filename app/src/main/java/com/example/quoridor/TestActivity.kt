package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityTestBinding
import com.example.quoridor.game.types.GameType
import com.example.quoridor.retrofit.RetrofitTestActivity

class TestActivity:  AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rankingBtn.setOnClickListener {
            goto(RankingActivity::class.java)
        }

        binding.retrofitBtn.setOnClickListener {
            goto(RetrofitTestActivity::class.java)
        }

        binding.localGameTest.setOnClickListener {
            val intent = Intent(this, GameForLocalActivity::class.java)
            intent.putExtra("gameType", GameType.BLITZ.ordinal)
            startActivity(intent)
        }

        binding.pvpGameTest.setOnClickListener {
            val intent = Intent(this, GameForPvPActivity::class.java)
            intent.putExtra("gameType", GameType.BLITZ.ordinal)
            startActivity(intent)
        }
    }

    private fun <T> goto(activityClass: Class<T>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

}