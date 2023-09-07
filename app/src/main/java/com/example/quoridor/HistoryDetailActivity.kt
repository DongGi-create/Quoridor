package com.example.quoridor

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.communication.retrofit.HttpSyncService.historyDetail
import com.example.quoridor.communication.retrofit.util.RetrofitFunc.buildRepeatJob
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.communication.retrofit.util.ToastHttpResult
import com.example.quoridor.databinding.ActivityHistoryDetailBinding
import com.example.quoridor.util.Func.popToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryDetailActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityHistoryDetailBinding.inflate(layoutInflater)
    }

    private val TAG = "Dirtfy Test - HistoryDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        HttpService().historyDetail(
//            intent.getLongExtra("gameId", 0),
//            SuccessfulHttpResult {
//                binding.apply{
//                    gameIdTextView.text = it.gameId.toString()
//                    uid0TextView.text = it.uid0.toString()
//                    uid1TextView.text = it.uid1.toString()
//                    score0TextView.text = it.score0.toString()
//                    score1TextView.text = it.score1.toString()
//                }
//            }
//        )

        HttpSyncService.execute {
            val historyDetail = historyDetail(intent.getLongExtra("gameId", 0))
            binding.apply{
                historyDetail.also {
                    if (it == null) return@also
                    gameIdTextView.text = it.gameId.toString()
                    uid0TextView.text = it.uid0.toString()
                    uid1TextView.text = it.uid1.toString()
                    score0TextView.text = it.score0.toString()
                    score1TextView.text = it.score1.toString()
                }
            }
        }
    }
}