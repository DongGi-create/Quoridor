package com.example.quoridor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoridor.adapter.RankingRecyclerViewAdapter
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.databinding.ActivityRankingBinding
import com.example.quoridor.login.UserManager
import com.example.quoridor.ranking.RankingViewModel

class RankingActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRankingBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[RankingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.loadFirstRanking {
            binding.rankingRecyclerView.apply {
                adapter = RankingRecyclerViewAdapter(viewModel.getList()) {
                    viewModel.loadMoreRanking {
                        if (it.isEmpty())
                            Toast.makeText(this@RankingActivity, "loaded all", Toast.LENGTH_SHORT).show()
                    }
                }
                layoutManager = LinearLayoutManager(
                    this@RankingActivity,
                    LinearLayoutManager.VERTICAL,
                    false)

                adapter!!.also { that ->
                    viewModel.rankingList.observeInsert {
                        that.notifyItemInserted(it)
                    }
                    viewModel.rankingList.observeRemove {
                        that.notifyItemRemoved(it)
                    }
                    viewModel.rankingList.observeChange {
                        that.notifyItemChanged(it)
                    }
                }
            }
        }

        viewModel.loadMyRanking {
            binding.apply {
                myRankingRankingTextView.text = it.toString()
                myRankingNameTextView.text = UserManager.umname
                myRankingRatingTextView.text = UserManager.umscore.toString()

                myRankingRatingImageView.setImageResource(
                    when (UserManager.umscore) {
                        in 0 until 1000 -> R.drawable.baseline_workspace_premium_24_purple
                        in 1000 until 1200 -> R.drawable.baseline_workspace_premium_24_yellow
                        in 1200 until 1500 -> R.drawable.baseline_workspace_premium_24_blue
                        else -> R.drawable.baseline_workspace_premium_24_green
                    }
                )
            }
        }

    }
}