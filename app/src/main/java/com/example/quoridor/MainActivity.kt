package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.ingame.CustomViewTestActivity
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.SharedLoginModel
import com.example.quoridor.login.UserManager

class MainActivity : AppCompatActivity() {

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var sharedLoginModel: SharedLoginModel
    private lateinit var loginmypageIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loginmypageIntent = Intent(this,LoginActivity::class.java)

        sharedLoginModel = ViewModelProvider(this).get(SharedLoginModel::class.java)

        sharedLoginModel.loginSuccess.observe(this) { success ->
            if(success){
                loginmypageIntent = Intent(this,MyPage::class.java)
                binding.ivMainLoginMypage.setImageResource(R.drawable.ic_mypage)
                binding.tvMainLoginMypage.setText("myPage")
            }else{
                loginmypageIntent = Intent(this,LoginActivity::class.java)
                binding.ivMainLoginMypage.setImageResource(R.drawable.ic_login)
                binding.tvMainLoginMypage.setText("Login")
            }
        }
        // 여기서 UserManager 정보 가져오기
        val user = UserManager.getInstance() // UserManager 클래스에 맞게 수정
        if (user.umid!="") {
            sharedLoginModel.setLoginSuccess(true)
        }
        else {
            sharedLoginModel.setLoginSuccess(false)
        }

        binding.ivMainGame.setOnClickListener {
            val intent = Intent(this, CustomViewTestActivity::class.java)
            startActivity(intent)
        }
        binding.ivMainLoginMypage.setOnClickListener{
            startActivity(loginmypageIntent)
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