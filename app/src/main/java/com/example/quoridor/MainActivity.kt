package com.example.quoridor

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.service.autofill.FieldClassification.Match
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.databinding.ActivityProgressBarTestBinding
import com.example.quoridor.databinding.DialogWaitingPvpBinding
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.SharedLoginModel
import com.example.quoridor.login.UserManager
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var sharedLoginModel: SharedLoginModel
    private lateinit var loginmypageIntent: Intent
    private lateinit var pvpDialog: MatchingDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loginmypageIntent = Intent(this, LoginActivity::class.java)

        sharedLoginModel = ViewModelProvider(this).get(SharedLoginModel::class.java)

        sharedLoginModel.loginSuccess.observe(this) { success ->
            if(success){
                loginmypageIntent = Intent(this,MyPage::class.java)
                binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_mypage)
            }else{
                loginmypageIntent = Intent(this,LoginActivity::class.java)
                binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_login)
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
            dialog()
        }
        binding.mainIcPuzzle.setOnClickListener{
            dialog()
        }

        binding.mainBtnTest.setOnClickListener {
            goto(TestActivity::class.java)
        }
    }
    private fun <T> goto(activityClass: Class<T>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }



    private fun dialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
        dialog.setContentView(R.layout.dialog_game_quit)

        val yesBtn = dialog.findViewById<Button>(R.id.yes_btn)
        val noBtn = dialog.findViewById<Button>(R.id.no_btn)

        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}