package com.example.quoridor.customView.gameBoardView

import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import com.example.quoridor.retrofit.util.RetrofitFunc
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

object Func {

    private val TAG = "Dirtfy Test"

    fun buildDelayedJob(delayTime: Long, listener: () -> Unit): Job {
        return CoroutineScope(Dispatchers.Default)
            .launch(start = CoroutineStart.LAZY) {
                delay(delayTime)
                withContext(Dispatchers.Main) {
                    listener()
                }
            }
    }

    fun View.setOnShortLongClickListener(listener: () -> Unit) {
        setOnTouchListener(object : View.OnTouchListener {

            private val longClickDuration = 200L
            private lateinit var delayJob: Job

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    delayJob = buildDelayedJob(longClickDuration) {
                        v?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        listener()
                    }
                    delayJob.start()
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    CoroutineScope(Dispatchers.Default)
                        .launch {
                            try {
                                supervisorScope {
                                    delayJob.cancel()
                                }
                            } catch (e: CancellationException) {
                                Log.d(TAG, "shortLongClick delayJob cancel")
                            }
                            finally {
                                Log.d(TAG, "shortLongClick action up")
                            }
                        }
                }
                return true
            }
        })
    }

}