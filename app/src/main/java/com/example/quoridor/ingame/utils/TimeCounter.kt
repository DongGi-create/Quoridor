package com.example.quoridor.ingame.utils

import android.app.Activity
import android.widget.TextView
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

            val min = left/(60 * 1000)
            val sec = left%(60 * 1000) / 1000
            val text = "$min:$sec"

            activity.runOnUiThread{
                time_tv.text = text
            }
        }
    }

    fun pause(){
        timerTask.cancel()
    }

    fun reset(){
        timerTask.cancel()

        left = duration
        val min = left/(60 * 1000)
        val sec = left%(60 * 1000) / 1000
        val text = "$min:$sec"
        time_tv.text = text
    }
}