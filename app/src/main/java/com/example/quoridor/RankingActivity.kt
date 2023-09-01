package com.example.quoridor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoridor.adapter.RankingRecyclerViewAdapter
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRankingBinding.inflate(layoutInflater)
    }

    private val rankingList = ArrayList<HttpDTO.Response.User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.myRankingNameTextView.text = "Dirtfy"
        binding.myRankingRatingTextView.text = "1250"
        binding.myRankingRankingTextView.text = "999+"
        binding.myRankingRatingImageView.setImageResource(R.drawable.baseline_workspace_premium_24_blue)

        /*rankingList.add(HttpDTO.SignUpResponse("", "", "", "create", "2000", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "dongWon", "1300", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "eoaegwgwegf", "1230", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "didwoah", "1200", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "WeGlonD", "1050", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "crediaaeagefaet", "1032", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "dongGi", "1000", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "minseok", "900", ""))
        rankingList.add(HttpDTO.SignUpResponse("", "", "", "zzammo", "800", ""))
*/
        binding.rankingRecyclerView.adapter = RankingRecyclerViewAdapter(rankingList)
        binding.rankingRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}