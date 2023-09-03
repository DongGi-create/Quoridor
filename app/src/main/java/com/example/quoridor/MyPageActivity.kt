package com.example.quoridor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.customView.rankingItemView
import com.example.quoridor.databinding.ActivityMypageBinding
import com.example.quoridor.login.UserManager
import com.google.api.Http
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
        val user = UserManager/*.getInstance() // UserManager 클래스에 맞게 수정*/
        binding.tvMyPageName.setText(user.umname)
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


                var idx = -1 // Initialize idx with a default value

                for ((index, rankUser) in it.rankingUserList.withIndex()) {
                    if (rankUser.uid == UserManager.umuid) {
                        idx = index
                        break
                    }
                }

                if (idx != -1) {
                    setValues(
                        binding.mypageCustomrankingUp,
                        myRanking - 1,
                        it.rankingUserList.getOrElse(idx - 1) { null })
                    setValues(
                        binding.mypageCustomrankingUp,
                        myRanking,
                        it.rankingUserList.getOrElse(idx) { null })
                    setValues(
                        binding.mypageCustomrankingUp,
                        myRanking + 1,
                        it.rankingUserList.getOrElse(idx + 1) { null })
                }
            }
        )
    }

    private fun setValues(v:rankingItemView, rank:Int, rankingUser: HttpDTO.Response.RankingUser?){
        if(rankingUser==null) return
        v.medalIv.setImageResource(getMedal(rankingUser.score))
        v.rankTv.text = rank.toString()
        v.nameTv.text = rankingUser.name
        v.ratingTv.text = rankingUser.score.toString()
    }

    private fun getMedal(score:Int):Int{
        return when(score?:0) {
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