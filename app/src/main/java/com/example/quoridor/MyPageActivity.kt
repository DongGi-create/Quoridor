package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpResult
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.customView.RankingItemView
import com.example.quoridor.databinding.ActivityMypageBinding
import com.example.quoridor.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import org.eazegraph.lib.models.ValueLinePoint
import org.eazegraph.lib.models.ValueLineSeries
import java.util.Calendar
import kotlin.math.roundToInt

class MyPageActivity:AppCompatActivity() {
    val binding:ActivityMypageBinding by lazy{
        ActivityMypageBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private val service = HttpService()
    private var profileLink:String? = null
    private var chart: PieChart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        chart = binding.pcMyPageWinningRate
        setPieChart()
        val user = UserManager // UserManager 클래스에 맞게 수정
        binding.tvMyPageName.text = user.umname
        binding.profileEmailTextView.text = user.umemail
        binding.btnMyPageLogout.setOnClickListener{
            user.umid = ""
            //user.setId("")
            val intent = Intent(this@MyPageActivity,MainActivity::class.java)
            startActivity(intent)
            this@MyPageActivity.finish()
            HttpSyncService.execute {
                logout()
            }
        }

        binding.mypageIvRankingDetail.setOnClickListener{
            startActivity(Intent(this@MyPageActivity,RankingActivity::class.java))
        }
        binding.mypageIvHistoryDetail.setOnClickListener{
            startActivity(Intent(this@MyPageActivity, HistoryActivity::class.java))
        }

        binding.mypageIvProfileDetail.setOnClickListener{
            val it = Intent(this@MyPageActivity, EditActivity::class.java)
            it.putExtra("profileLinkKey", profileLink)
            startActivity(it)
            this@MyPageActivity.finish()
        }

        service.getImage(UserManager.umuid!!,object: HttpResult<String> {
            override fun success(data: String) {
                Log.d(TAG,"getImage success!")
                if(data!="null"){
                    profileLink = data
                    Glide.with(this@MyPageActivity)
                        .load(data) //이미지
                        .into(binding.mypageIvProfile) //보여줄 위치
                }
                else binding.mypageIvProfile.setImageResource(R.drawable.ic_identity)
            }
            override fun appFail() {
                Log.d(TAG,"app fail")
            }
            override fun fail(throwable: Throwable) {
                Log.d(TAG,throwable.message+"")
            }
            override fun finally() {
            }
        })

        service.adjacentRanking(
            SuccessfulHttpResult{
                val frontRanking = it.firstElementRank.split("/")[0].toInt()

                var idx = -1 // Initialize idx with a default value

                for ((index, rankUser) in it.rankingUserList.withIndex()) {
                    if (rankUser.uid == UserManager.umuid) {
                        idx = index
                        break
                    }
                }

                if (idx != -1) {
                    setValues(
                        binding.mypageCustomRank1,
                        frontRanking + idx - 1,
                        it.rankingUserList.getOrElse(idx - 1) { null })
                    setValues(
                        binding.mypageCustomRank2,
                        frontRanking + idx,
                        it.rankingUserList.getOrElse(idx) { null })
                    setValues(
                        binding.mypageCustomRank3,
                        frontRanking + idx + 1,
                        it.rankingUserList.getOrElse(idx + 1) { null })
                }
            }
        )
        /*binding.mypageCustomrankingDown.medalIv.setImageResource(R.drawable.baseline_workspace_premium_24_purple)
        binding.mypageCustomrankingDown.rankTv.text = "200"
        binding.mypageCustomrankingDown.nameTv.text="d"
        binding.mypageCustomrankingDown.ratingTv.text = "2000"*/

        setHistoryPart()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@MyPageActivity,MainActivity::class.java)
        startActivity(intent)
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
        val winGames = UserManager.umwinGames
        val totalGames = UserManager.umtotalGames

        val winningRate = if (winGames != null && totalGames != null && totalGames != 0) {
            winGames.toFloat() / totalGames.toFloat() * 100
        } else {
            (0.0).toFloat()
        }

        Log.d(TAG,""+winningRate)
        chart!!.addPieSlice(PieModel("Win", winningRate, getColor(R.color.D_blue)))
        chart!!.addPieSlice(PieModel("Lose", 100-winningRate, getColor(R.color.D_red)))
        chart!!.startAnimation()
    }

    private fun setHistoryPart() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 1)
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)+1
        val day = cal.get(Calendar.DATE)
        val recentGameId = ((year*10000 + month*100 + day)*(10e+11)).toLong()

        HttpSyncService.execute {
            val histories = histories(recentGameId)!!
            val ratingList = mutableListOf<Int>()

            for (history in histories) {
                val historyDetail = historyDetail(history.gameId)!!

                if (historyDetail.uid0 == UserManager.umuid)
                    ratingList.add(historyDetail.score0)
                else
                    ratingList.add(historyDetail.score1)
            }

            val valueLineSeries = ValueLineSeries()

            val minRating = ratingList.min()
            val maxRating = ratingList.max()
            for (rating in ratingList.reversed()) {
                valueLineSeries.addPoint(ValueLinePoint(
                    "$rating",
                    (rating-minRating)*0.8f/(maxRating-minRating + 0.00001f)+0.1f)
                )
                Log.d("MyPageActivity setHistoryPart",
                    "rating: $rating - ${(rating-minRating)*0.8f/(maxRating-minRating + 0.00001f)+0.1f}")
            }

            withContext(Dispatchers.Main) {
                valueLineSeries.color = getColor(R.color.D_blue)
                binding.winingRateGraph.apply {
                    addSeries(valueLineSeries)
                    startAnimation()
                }
            }
        }

        val winGames = UserManager.umwinGames!!
        val totalGames = UserManager.umtotalGames!!

        val winRatio = if (totalGames == 0) 1f else winGames.toFloat()/totalGames
        val loseRatio = 1-winRatio

        binding.pcMyPageWinningRate.apply {
            addPieSlice(PieModel(
                "Win",
                winRatio,
                getColor(R.color.D_blue)))
            addPieSlice(PieModel(
                "Lose",
                loseRatio,
                getColor(R.color.D_red)))

            startAnimation()
        }

        binding.pcMyPageWinningRate.innerValueString = "${((winRatio*10000).roundToInt()/100f)}%"

        binding.totalGameTextView.text = totalGames.toString()
    }

}