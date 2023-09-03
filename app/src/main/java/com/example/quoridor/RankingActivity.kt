package com.example.quoridor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoridor.adapter.RankingRecyclerViewAdapter
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRankingBinding.inflate(layoutInflater)
    }

    private val service = HttpService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        service.underRanking(
            -1,
            SuccessfulHttpResult{
                binding.rankingRecyclerView.apply {
                    adapter = RankingRecyclerViewAdapter(it.rankingUserList.toMutableList())
                    layoutManager = LinearLayoutManager(this@RankingActivity, LinearLayoutManager.VERTICAL, false)
                }
            })


    }
}