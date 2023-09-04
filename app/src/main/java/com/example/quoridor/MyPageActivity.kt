package com.example.quoridor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.customView.RankingItemView
import com.example.quoridor.databinding.ActivityMypageBinding
import com.example.quoridor.login.UserManager
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class MyPageActivity:AppCompatActivity() {
    val binding:ActivityMypageBinding by lazy{
        ActivityMypageBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private val service = HttpService()

    private var chart: PieChart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        chart = binding.pcMyPageWinningRate
        setPieChart()
        val user = UserManager // UserManager 클래스에 맞게 수정
        binding.tvMyPageName.text = user.umname
        binding.btnMyPageLogout.setOnClickListener{
            user.umid = ""
            //user.setId("")
            val intent = Intent(this@MyPageActivity,MainActivity::class.java)
            startActivity(intent)
            //세션 끊기 넣기
        }

        binding.mypageIvRankingDetail.setOnClickListener{
            startActivity(Intent(this@MyPageActivity,RankingActivity::class.java))
        }
        binding.mypageIvHistoryDetail.setOnClickListener{
            startActivity(Intent(this@MyPageActivity, HistoryActivity::class.java))
        }

        service.adjacentRanking(
            SuccessfulHttpResult{
                val myRanking = it.firstElementRank.split("/")[0].toInt()
                Log.d(TAG,"adjacent")


                var idx = -1 // Initialize idx with a default value

                for ((index, rankUser) in it.rankingUserList.withIndex()) {
                    if (rankUser.uid == UserManager.umuid) {
                        idx = index
                        break
                    }
                }

                if (idx != -1) {
                    Log.d(TAG,"in if")
                    setValues(
                        binding.mypageCustomRank1,
                        myRanking - 1,
                        it.rankingUserList.getOrElse(idx - 1) { null })
                    setValues(
                        binding.mypageCustomRank2,
                        myRanking,
                        it.rankingUserList.getOrElse(idx) { null })
                    setValues(
                        binding.mypageCustomRank3,
                        myRanking + 1,
                        it.rankingUserList.getOrElse(idx + 1) { null })
                }
            }
        )
        /*binding.mypageCustomrankingDown.medalIv.setImageResource(R.drawable.baseline_workspace_premium_24_purple)
        binding.mypageCustomrankingDown.rankTv.text = "200"
        binding.mypageCustomrankingDown.nameTv.text="d"
        binding.mypageCustomrankingDown.ratingTv.text = "2000"*/
    }

    private fun setValues(v: RankingItemView, rank:Int, rankingUser: HttpDTO.Response.RankingUser?){
        Log.d(TAG,"set values")
        if(rankingUser == null) return
        v.ivRating.setImageResource(getMedal(rankingUser.score))
        v.tvRank.text = rank.toString()
        v.tvName.text = rankingUser.name
        v.tvRating.text = rankingUser.score.toString()
    }

    private fun getMedal(score:Int):Int{
        return when(score) {
            in 0 until 1000 -> R.drawable.baseline_workspace_premium_24_purple
            in 1000 until 1200 -> R.drawable.baseline_workspace_premium_24_yellow
            in 1200 until 1500 -> R.drawable.baseline_workspace_premium_24_blue
            else -> R.drawable.baseline_workspace_premium_24_green
        }
    }
    private fun setPieChart() {
        chart!!.clearChart()
        chart!!.addPieSlice(PieModel("Win", 60f, Color.parseColor("#FF5454")))
        chart!!.addPieSlice(PieModel("Lose", 40f, Color.parseColor("#54A7FF")))
        chart!!.startAnimation()
    }
}