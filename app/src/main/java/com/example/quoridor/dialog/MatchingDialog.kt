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
import com.example.quoridor.communication.retrofit.HttpSyncService
import com.example.quoridor.communication.retrofit.util.RetrofitFunc.buildRepeatJob
import com.example.quoridor.game.types.GameType
import com.example.quoridor.util.Func
import com.example.quoridor.util.Func.startGameActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MatchingDialog(context:Context, val gameType: Int): Dialog(context) {
    private var minuteMills = (5 * 60000).toLong()
    private var progressBarCircle: ProgressBar? = null
    private var remainTime: TextView? = null
    private val httpService = HttpService()
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var keepTryingJob: Deferred<HttpDTO.Response.Match?>
    private lateinit var gameStartJob: Job
    private lateinit var matchingResult:Deferred<HttpDTO.Response.Match?>
    private lateinit var matchingJob: Job
    private lateinit var enumGameType: GameType

    private val TAG: String by lazy {
        context.getString(R.string.Dirtfy_test_tag)//context안에 있다
    }
    init{
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(context.getColor(R.color.D_transparent)))
        setContentView(R.layout.dialog_waiting_pvp)
        progressBarCircle = findViewById(R.id.waiting_progressBarCircle)
        remainTime = findViewById(R.id.waiting_tv_remaintime)

        enumGameType = when(gameType){
            0-> GameType.STANDARD
            1-> GameType.CLASSIC
            else -> {GameType.BLITZ} //2
        }
        val closeBtn = findViewById<ImageView>(R.id.waiting_iv_close)
        closeBtn.setOnClickListener {
            Log.d("oz","matching dialog closeBtn clicked")
            HttpSyncService.execute {
                Log.d("oz","$gameType send")
                val result = exitMatching(gameType)
                Log.d("oz",result.toString())
                dismiss()

                if (result != null) {
                    //Func.popToast(context, "매칭성공! GameID: $result")
                    val intent = Intent(context, GameForPvPActivity::class.java)
                    context.startGameActivity(intent, enumGameType, result)
                }
            }

        }
    }

    override fun show() {
        setProgressBarValues()
        super.show()
        startMatching(gameType)
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

//    fun startMatching(){
//        if (!this@MatchingDialog::keepTryingJob.isInitialized || !keepTryingJob.isActive) {
//            val matchData = HttpDTO.Request.Match(3)
//
//            keepTryingJob = RetrofitFunc.buildKeepTryingJob(
//                matchData,
//                httpService::makeMatchCall,
//                ToastHttpResult(context, "matching", TAG),
//                {
//                    !(it != null && it.gameId == null || it.turn == null)
//                })
//
//            gameStartJob = buildGameStartJob(keepTryingJob)
//
//            gameStartJob.start()
//            keepTryingJob.start()
//        }
//    }

    override fun dismiss() {
        countDownTimer.cancel()
        if (this@MatchingDialog::matchingJob.isInitialized && matchingJob.isActive)
            matchingJob.cancel()
        if (this@MatchingDialog::matchingResult.isInitialized && matchingResult.isActive)
            matchingResult.cancel()
        super.dismiss()
    }

//    override fun onStop() {
//        super.onStop()
//        if (this@MatchingDialog::keepTryingJob.isInitialized && keepTryingJob.isActive) {
//            keepTryingJob.cancel()
//        }
//        if (this@MatchingDialog::gameStartJob.isInitialized && gameStartJob.isActive) {
//            gameStartJob.cancel()
//        }
//    }
//    private fun buildGameStartJob(keepTryingJob: Deferred<HttpDTO.Response.Match?>): Job {
//        return CoroutineScope(Dispatchers.Main)
//            .launch(start = CoroutineStart.LAZY) {
//                Log.d(TAG, "gameStartJob start")
//                val matchData = keepTryingJob.await()!!
//                Log.d(TAG, "gameStartJob awaited $matchData")
//                Log.d(TAG, "gameStartJob end")
//
//                if (matchData.gameId == null) {
//                    Func.popToast(context, "매칭 실패")
//                }
//                else {
//                    Func.popToast(context, "매칭성공! GameID: $matchData")//await는 비동기로만 받을 수 있다
//                    dismiss()
//                    val intent = Intent(context, GameForPvPActivity::class.java)
//                    context.startGameActivity(intent, GameType.BLITZ, matchData)
//                }
//            }
//    }

    private fun isMatched(response: HttpDTO.Response.Match?): Boolean {
        return response != null && !(response.gameId == null || response.turn == null)
    }
    private fun startMatching(gameType: Int) {
        matchingJob = CoroutineScope(Dispatchers.Default).launch {
            var result: String? = null
            HttpSyncService.execute {
                result = match(gameType)
            }.join()

            if (result != "OK")
                cancel()

            matchingResult = buildRepeatJob(
                -1,
                5000L,
                this@MatchingDialog::isMatched
            ) {
                var matchResponse: HttpDTO.Response.Match? = null

                HttpSyncService.execute {
                    matchResponse = matchCheck()
                }.join()

                matchResponse
            }
            val matchingResultAwait = matchingResult.await()

            withContext(Dispatchers.Main) {
                if (isMatched(matchingResultAwait)) {
                    //Func.popToast(context, "매칭성공! GameID: $matchingResult")
                    dismiss()
                    val intent = Intent(context, GameForPvPActivity::class.java)
                    context.startGameActivity(intent, enumGameType, matchingResultAwait!!)
                } else {
                    Func.popToast(context, "매칭 실패")
                }
            }
        }
    }
}