package com.example.quoridor.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.game.types.GameType
import com.example.quoridor.game.util.GameFunc.putGameType
import com.example.quoridor.game.util.GameFunc.putMatchData
import com.google.gson.Gson
import kotlin.math.pow
import kotlin.math.sqrt

object Func {

    private const val userTag = "userData"

    fun <T> Array<Array<T>>.get(cor: Coordinate): T {
        return this[cor.r][cor.c]
    }
    fun <T> Array<Array<T>>.set(cor: Coordinate, value: T) {
        this[cor.r][cor.c] = value
    }

    fun <T> Intent.putAny(tag: String, data: T) {
        this.putExtra(tag, Gson().toJson(data))
    }
    fun <T> Intent.getAny(tag: String, cls: Class<T>): T? {
        val str = this.getStringExtra(tag)
        return if (str == null)
            null
        else
            Gson().fromJson(str, cls)
    }

    fun Intent.putUser(user: HttpDTO.Response.User) {
        this.putExtra(userTag, Gson().toJson(user))
    }
    fun Intent.getUser(): HttpDTO.Response.User? {
        val str = this.getStringExtra(userTag)
        return if (str == null)
            null
        else
            Gson().fromJson(str, HttpDTO.Response.User::class.java)
    }

    fun Context.startGameActivity(intent: Intent, gameType: GameType) {
        intent.putGameType(gameType)
        this.startActivity(intent)
    }
    fun Context.startGameActivity(intent: Intent, gameType: GameType, matchData: HttpDTO.Response.Match) {
        intent.putGameType(gameType)
        intent.putMatchData(matchData)
        this.startActivity(intent)
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