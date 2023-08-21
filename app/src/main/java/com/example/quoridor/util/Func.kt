package com.example.quoridor.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlin.math.pow
import kotlin.math.sqrt

object Func {

    fun <T> Array<Array<T>>.get(cor: Coordinate): T {
        return this[cor.r][cor.c]
    }
    fun <T> Array<Array<T>>.set(cor: Coordinate, value: T) {
        this[cor.r][cor.c] = value
    }

    fun View.move(newParent: ViewGroup) {
        (this.parent as? ViewGroup)?.removeView(this)
        newParent.addView(this)
    }

    fun View.removeFromParent() {
        (this.parent as? ViewGroup)?.removeView(this)
    }

    fun View.setSize(width: Int, height: Int) {
        val lp = this.layoutParams
        lp.width = width
        lp.height = height
        this.layoutParams = lp
    }

    fun View.setSize(ref: View) {
        val lp = this.layoutParams
        lp.width = ref.layoutParams.width
        lp.height = ref.layoutParams.height
        this.layoutParams = lp
    }

    fun distance(p1: Pair<Float, Float>, p2: Pair<Float, Float>): Float{
        return sqrt((p1.first-p2.first).pow(2) + (p1.second-p2.second).pow(2))
    }
    fun millToMinSec(time: Long): String {
        val minInt = time / (60 * 1000)
        val min = when(minInt) {
            in 0..9 -> "0${minInt}"
            else -> "$minInt"
        }

        val secInt = time % (60 * 1000) / 1000
        val sec = when(secInt) {
            in 0..9 -> "0${secInt}"
            else -> "$secInt"
        }

        return "$min:$sec"
    }

    fun popToast(context: Context, content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }

}