package com.example.quoridor.ingame.utils

import android.app.Activity
import android.widget.TextView
import java.util.Timer
import kotlin.concurrent.timer

class TimeCounter(
    val activity: Activity,
    val time_tv: TextView,
    val duration: Long
    ) {

    private var left = duration
    private lateinit var timerTask: Timer
    private var period = 1000L

    fun start(){
        timerTask = timer(period = period){
            left -= period

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
        time_tv.text = "10:00"
    }
}