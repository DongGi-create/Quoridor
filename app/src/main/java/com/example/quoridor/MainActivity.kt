package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.ingame.CustomViewTestActivity
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.SharedViewModel
import com.example.quoridor.login.UserManager

class MainActivity : AppCompatActivity() {

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
       ///////////////////////////////////////////////////////////
        sharedViewModel.loginSuccess.observe(this) { success ->
            Log.d(TAG, "!!!!!")
            Toast.makeText(this@MainActivity, "login changed", Toast.LENGTH_SHORT).show()

        }
        // 여기서 UserManager 정보 가져오기
        val user = UserManager.getInstance() // UserManager 클래스에 맞게 수정

        // 로그인 여부에 따라서 loginSuccess 값 설정
        if (user.umid!="") {
            sharedViewModel.setLoginSuccess(true)
        }
        ///////////////////////////////////////////////////////////

        binding.ivMainGame.setOnClickListener {
            val intent = Intent(this, CustomViewTestActivity::class.java)
            startActivity(intent)
        }
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