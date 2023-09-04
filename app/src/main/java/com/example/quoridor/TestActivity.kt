package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.communication.retrofit.RetrofitTestActivity
import com.example.quoridor.communication.socket.SocketTestActivity
import com.example.quoridor.databinding.ActivityTestBinding
import com.example.quoridor.game.types.GameType
import com.example.quoridor.game.util.GameFunc.putGameType

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
            intent.putGameType(GameType.BLITZ)
            startActivity(intent)
        }

        binding.pvpGameTest.setOnClickListener {
//            val intent = Intent(this, TestPvpActivity::class.java)
//            intent.putGameType(GameType.BLITZ)
//            intent.putMatchData(HttpDTO.MatchingResponse("gameId", 1))
//            startActivity(intent)
        }

        binding.socketTest.setOnClickListener {
            goto(SocketTestActivity::class.java)
        }

        binding.pieChart.setOnClickListener{
            //goto(ProgressBarTest::class.java)
        }

        binding.history.setOnClickListener {
            goto(HistoryActivity::class.java)
        }

        binding.mypage.setOnClickListener{
            goto(MyPageActivity::class.java)
        }

        binding.profile.setOnClickListener{
            goto(ProfileTest::class.java)
        }
    }

    private fun <T> goto(activityClass: Class<T>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

}