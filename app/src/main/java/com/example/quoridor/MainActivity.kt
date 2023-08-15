package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.ingame.CustomViewTestActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivMainGame.setOnClickListener {
            val intent = Intent(this, CustomViewTestActivity::class.java)
            startActivity(intent)
        }
        /*binding.ivMainRanking.setOnClickListener{

        }*/
        binding.ivMainLoginMypage.setOnClickListener{
            /*val intent = Intent(this, MyPage::class.java)
            startActivity(intent)*/
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.ivMainRanking.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }

        binding.testBtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }
    }
}


//        binding.btnGamestart.setOnClickListener {
//            val intent = Intent(this, Game1v1::class.java)
//            startActivity(intent)
//        }
//
//        binding.btnAuthtest.setOnClickListener {
//            val intent = Intent(this, test::class.java)
//            startActivity(intent)
//        }

/*binding.retrofitTestBtn.setOnClickListener {
    val intent = Intent(this, RetrofitTestActivity::class.java)
    startActivity(intent)
}*/