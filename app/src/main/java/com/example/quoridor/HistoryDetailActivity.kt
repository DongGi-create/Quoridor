package com.example.quoridor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.databinding.ActivityHistoryDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryDetailActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityHistoryDetailBinding.inflate(layoutInflater)
    }

    private val TAG = "Dirtfy Test - HistoryDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        HttpService().historyDetail(
            intent.getLongExtra("gameId", 0),
            SuccessfulHttpResult {
                binding.apply{
                    gameIdTextView.text = it.gameId.toString()
                    uid0TextView.text = it.uid0.toString()
                    uid1TextView.text = it.uid1.toString()
                    score0TextView.text = it.score0.toString()
                    score1TextView.text = it.score1.toString()
                }
            }
        )
    }
}