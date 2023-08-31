package com.example.quoridor

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.concurrent.TimeUnit

class MatchingDialog(context:Context): Dialog(context) {
    private var minuteMills = (5 * 60000).toLong()
    private var progressBarCircle: ProgressBar? = null
    private var remainTime: TextView? = null
    private lateinit var countDownTimer: CountDownTimer

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
        startCountDownTimer()



    }

    override fun onStop() {
        super.onStop()
        countDownTimer.cancel()
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
}