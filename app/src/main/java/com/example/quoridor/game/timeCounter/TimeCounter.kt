package com.example.quoridor.game.timeCounter

import android.app.Activity
import android.widget.TextView
import com.example.quoridor.util.Func
import java.util.Timer
import kotlin.concurrent.timer

class TimeCounter(
    private val activity: Activity,
    private val time_tv: TextView,
    private val duration: Long,
    private val timeOverListener: TimeCounterOverListener
    ) {

    var left = duration
    private lateinit var timerTask: Timer
    private var period = 1000L

    fun start(){
        if (left <= 0L) return

        timerTask = timer(period = period){
            left -= period

            if (left == 0L) {
                activity.runOnUiThread {
                    timeOverListener.timeOver()
                }
                this.cancel()
            }

            activity.runOnUiThread{
                time_tv.text = Func.millToMinSec(left)
            }
        }
    }

    fun pause(){
        timerTask.cancel()
    }

    fun reset(){
        timerTask.cancel()

        left = duration
        val text = Func.millToMinSec(left)
        time_tv.text = text
    }
}