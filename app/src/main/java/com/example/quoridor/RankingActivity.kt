package com.example.quoridor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoridor.adapter.RankingRecyclerViewAdapter
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.databinding.ActivityRankingBinding
import com.example.quoridor.login.UserManager

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

        service.underRanking(
            UserManager.umuid!!,
            SuccessfulHttpResult{
                val myRanking = it.firstElementRank.split("/")[0]

                binding.myRankingRankingTextView.text = myRanking

                binding.myRankingNameTextView.text = UserManager.umname
                binding.myRankingRatingTextView.text = UserManager.umscore?.toString()

                binding.myRankingRatingImageView.setImageResource(
                    when(UserManager.umscore?:0) {
                        in 0 until 1000 -> R.drawable.baseline_workspace_premium_24_purple
                        in 1000 until 1200 -> R.drawable.baseline_workspace_premium_24_yellow
                        in 1200 until 1500 -> R.drawable.baseline_workspace_premium_24_blue
                        else -> R.drawable.baseline_workspace_premium_24_green
                    }
                )
            }
        )
    }
}