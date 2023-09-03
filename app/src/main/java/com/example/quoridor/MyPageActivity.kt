package com.example.quoridor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        binding.cvMyPageVs4tier.setOnClickListener{
            startActivity(Intent(this@MyPageActivity, HistoryActivity::class.java))
        }
    }

    private fun setPieChart() {
        chart!!.clearChart()
        chart!!.addPieSlice(PieModel("Win", 60f, Color.parseColor("#FF5454")))
        chart!!.addPieSlice(PieModel("Lose", 40f, Color.parseColor("#54A7FF")))
        chart!!.startAnimation()
    }
}