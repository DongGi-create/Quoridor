package com.example.quoridor

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quoridor.databinding.ActivityLoginBinding
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.dialog.MatchingDialog
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.SharedLoginModel
import com.example.quoridor.login.UserManager


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var sharedLoginModel: SharedLoginModel
    private lateinit var loginmypageIntent: Intent
    private lateinit var pvpDialog: MatchingDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainIvLoginMyPage.setOnClickListener{
            startActivity(loginmypageIntent)
        }

        binding.mainIvRanking.setOnClickListener{
            goto(RankingActivity::class.java)
        }

        binding.mainIcLocal.setOnClickListener {
            goto(GameForLocalActivity::class.java)
        }
        binding.mainIcPvp.setOnClickListener{
            pvpDialog = MatchingDialog(this)
            pvpDialog.show()

        }

        binding.mainIcAi.setOnClickListener{
            comingSoonDialog()
        }
        binding.mainIcPuzzle.setOnClickListener{
            comingSoonDialog()
        }

        binding.mainBtnTest.setOnClickListener {
            goto(TestActivity::class.java)
        }
    }
    private fun <T> goto(activityClass: Class<T>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }


    override fun onResume() {//액티비티를 넘어갈 때 finish를 해주는 것이 좋다.
        //또한 다른 액티비티에 넘어갔다가 뒤로가기를 누르거나하면 oncreate가 호출되는 것이 아니라 onResume가 호출이 된다.
        super.onResume()
        /*sharedLoginModel = ViewModelProvider(this).get(SharedLoginModel::class.java)

        sharedLoginModel.loginSuccess.observe(this) { success ->
            if(success){
                loginmypageIntent = Intent(this,MyPageActivity::class.java)
                binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_mypage)
            }else{
                loginmypageIntent = Intent(this,LoginActivity::class.java)
                binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_login)
            }
        }

        // 여기서 UserManager 정보 가져오기
        *//*val user = UserManager.getInstance() // UserManager 클래스에 맞게 수정*//*
        val user = UserManager
        if (user.umid!="") {
            sharedLoginModel.setLoginSuccess(true)
        }
        else {
            sharedLoginModel.setLoginSuccess(false)
        }*/
        val user = UserManager
        if (user.umid!="") {
            loginmypageIntent = Intent(this,MyPageActivity::class.java)
            binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_mypage)
        }else{
            loginmypageIntent = Intent(this,LoginActivity::class.java)
            binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_login)
        }
    }

    private fun comingSoonDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
        dialog.setContentView(R.layout.dialog_coming_soon)

        val yesBtn = dialog.findViewById<Button>(R.id.ok_btn)

        yesBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}