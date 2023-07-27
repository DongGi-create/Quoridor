package com.example.quoridor.ingame

import android.widget.ImageView
import com.example.quoridor.domain.Player
import com.example.quoridor.domain.utils.PlayerColorType

class FrontPlayer (
    val image: ImageView,
    row: Int,
    col: Int,
    uid: String,
    playerColorType: PlayerColorType
): Player(10, 10 * 60 * 1000, row, col, uid, playerColorType) {

}