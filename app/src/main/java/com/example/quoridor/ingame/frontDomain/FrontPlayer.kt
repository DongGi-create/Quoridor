package com.example.quoridor.ingame.frontDomain

import android.widget.ImageView
import com.example.quoridor.domain.Player
import com.example.quoridor.domain.utils.PlayerColorType
import com.example.quoridor.ingame.utils.TimeCounter

class FrontPlayer(
    val imageView: ImageView,
    private val timer: TimeCounter,
    left_wall: Int,
    left_ms: Long,
    row: Int,
    col: Int,
    uid: String,
    playerColorType: PlayerColorType
): Player(left_wall, left_ms, row, col, uid, playerColorType) {

    constructor(imageView: ImageView, timer: TimeCounter,p: Player):
            this(imageView, timer, p.left_wall, p.left_ms, p.row, p.col, p.uid, p.playerColorType)

    fun turnStart(){
        timer.start()
    }
    fun turnEnd(){
        timer.pause()
        left_ms = timer.left
    }

    fun setInfoView(){

    }
}