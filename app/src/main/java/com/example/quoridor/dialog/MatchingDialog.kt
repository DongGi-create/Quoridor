package com.example.quoridor.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.quoridor.GameForPvPActivity
import com.example.quoridor.R
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.util.Func
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.example.quoridor.communication.retrofit.util.RetrofitFunc
import com.example.quoridor.communication.retrofit.util.ToastHttpResult
import com.example.quoridor.game.types.GameType
import com.example.quoridor.util.Func.startGameActivity

class MatchingDialog(context:Context): Dialog(context) {
    private var minuteMills = (5 * 60000).toLong()
    private var progressBarCircle: ProgressBar? = null
    private var remainTime: TextView? = null
    private val httpService = HttpService()
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var keepTryingJob: Deferred<HttpDTO.Response.Match?>
    private lateinit var gameStartJob: Job

    private val TAG: String by lazy {
        context.getString(R.string.Dirtfy_test_tag)//context안에 있다
    }
    init{
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(context.getColor(R.color.D_transparent)))
        setContentView(R.layout.dialog_waiting_pvp)
        progressBarCircle = findViewById(R.id.waiting_progressBarCircle)
        remainTime = findViewById(R.id.waiting_tv_remaintime)


        val closeBtn = findViewById<ImageView>(R.id.waiting_iv_close)
        closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun show() {
        setProgressBarValues()
        super.show()
        startMatching()
        startCountDownTimer()
    }

    private fun setProgressBarValues() {
        progressBarCircle!!.max = minuteMills.toInt() / 1000
        progressBarCircle!!.progress = minuteMills.toInt() / 1000
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(minuteMills, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainTime!!.text = hmsTimeFormatter(millisUntilFinished)
                progressBarCircle!!.progress = (millisUntilFinished / 1000).toInt()
            }
            override fun onFinish() {
                //remainTime!!.text = hmsTimeFormatter(minuteMills)
                dismiss()
            }
        }
        countDownTimer.start()
    }

    private fun hmsTimeFormatter(milliSeconds: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milliSeconds
                )
            ),//분
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )//초
        )
    }

    fun startMatching(){
        if (!this@MatchingDialog::keepTryingJob.isInitialized || !keepTryingJob.isActive) {
            val matchData = HttpDTO.Request.Match(3)

            keepTryingJob = RetrofitFunc.buildKeepTryingJob(
                matchData,
                httpService::makeMatchCall,
                ToastHttpResult(context, "matching", TAG),
                {
                    !(it.gameId == null || it.turn == null)
                })

            gameStartJob = buildGameStartJob(keepTryingJob)

            gameStartJob.start()
            keepTryingJob.start()
        }
    }

    override fun onStop() {
        super.onStop()
        countDownTimer.cancel()
        if (this@MatchingDialog::keepTryingJob.isInitialized && keepTryingJob.isActive) {
            keepTryingJob.cancel()
        }
        if (this@MatchingDialog::gameStartJob.isInitialized && gameStartJob.isActive) {
            gameStartJob.cancel()
        }
    }
    private fun buildGameStartJob(keepTryingJob: Deferred<HttpDTO.Response.Match?>): Job {
        return CoroutineScope(Dispatchers.Main)
            .launch(start = CoroutineStart.LAZY) {
                Log.d(TAG, "gameStartJob start")
                val matchData = keepTryingJob.await()!!
                Log.d(TAG, "gameStartJob awaited $matchData")
                Log.d(TAG, "gameStartJob end")

                if (matchData.gameId == null) {
                    Func.popToast(context, "매칭 실패")
                }
                else {
                    Func.popToast(context, "매칭성공! GameID: $matchData")//await는 비동기로만 받을 수 있다
                    dismiss()
                    val intent = Intent(context, GameForPvPActivity::class.java)
                    context.startGameActivity(intent, GameType.BLITZ, matchData)
                }
            }
    }
}