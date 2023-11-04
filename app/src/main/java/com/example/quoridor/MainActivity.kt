package com.example.quoridor

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.dialog.MatchingDialog
import com.example.quoridor.game.types.GameType
import com.example.quoridor.game.util.GameFunc.putGameType
import com.example.quoridor.login.LoginActivity
import com.example.quoridor.login.UserManager


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val TAG: String by lazy {
        getString(R.string.Minseok_test_tag)
    }

    private lateinit var loginmypageIntent: Intent
    private lateinit var pvpDialog: MatchingDialog
    val user = UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.mainIvLoginMyPage.setOnClickListener{
            startActivity(loginmypageIntent)
            this.finish()
        }

        binding.mainIvRanking.setOnClickListener{
            if (user.umid!="") {
                goto(RankingActivity::class.java)
            }
            else{
                customDialog("Need Login!")
            }
        }

        binding.mainIcLocal.setOnClickListener {
            gameTypeSelectDialog(
                {
                    val intent = Intent(this@MainActivity, GameForLocalActivity::class.java)
                    intent.putGameType(GameType.BLITZ)
                    startActivity(intent)
                },
                {
                    val intent = Intent(this@MainActivity, GameForLocalActivity::class.java)
                    intent.putGameType(GameType.STANDARD)
                    startActivity(intent)
                },
                {
                    val intent = Intent(this@MainActivity, GameForLocalActivity::class.java)
                    intent.putGameType(GameType.CLASSIC)
                    startActivity(intent)
                },
                {
                    this@MainActivity.finish()
                }
            )
        }
        binding.mainIcPvp.setOnClickListener{
//            pvpDialog = MatchingDialog(this)
//            pvpDialog.show()

            gameTypeSelectDialog(
                {},
                {},
                {},
                {
                    pvpDialog = MatchingDialog(this)
                    pvpDialog.show()
                }
            )

        }

        binding.mainIcAi.setOnClickListener{
            customDialog("Coming Soon!")
        }
        binding.mainIcPuzzle.setOnClickListener{
            customDialog("Coming Soon!")
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

        if (user.umid!="") {
            loginmypageIntent = Intent(this,MyPageActivity::class.java)
            binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_mypage)
        }else{
            loginmypageIntent = Intent(this,LoginActivity::class.java)
            binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_login)
        }
    }


    private fun customDialog(texts: String){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
        dialog.setContentView(R.layout.dialog_coming_soon)
        val text = dialog.findViewById<TextView>(R.id.dcs_dialog_text)
        text.text = texts
        val yesBtn = dialog.findViewById<Button>(R.id.ok_btn)
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

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
//        val user = UserManager
//        if (user.umid!="") {
//            loginmypageIntent = Intent(this,MyPageActivity::class.java)
//            binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_mypage)
//        }else{
//            loginmypageIntent = Intent(this,LoginActivity::class.java)
//            binding.mainIvLoginMyPage.setImageResource(R.drawable.ic_login)
//        }
//    }

    private fun gameTypeSelectDialog(
        atType1: () -> Unit,
        atType2: () -> Unit,
        atType3: () -> Unit,
        finally: () -> Unit = {}) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(R.color.D_transparent)))
        dialog.setContentView(R.layout.dialog_game_type_select)

        val type1 = dialog.findViewById<Button>(R.id.type_1_button)
        val type2 = dialog.findViewById<Button>(R.id.type_2_button)
        val type3 = dialog.findViewById<Button>(R.id.type_3_button)

        type1.setOnClickListener {
            dialog.dismiss()
            atType1()
            finally()
            dialog.dismiss()
        }

        type2.setOnClickListener {
            dialog.dismiss()
            atType2()
            finally()
            dialog.dismiss()
        }

        type3.setOnClickListener {
            dialog.dismiss()
            atType3()
            finally()
            dialog.dismiss()
        }

        dialog.show()

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