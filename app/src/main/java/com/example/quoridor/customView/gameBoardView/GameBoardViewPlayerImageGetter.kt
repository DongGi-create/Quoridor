package com.example.quoridor.customView.gameBoardView

import android.widget.ImageView

interface GameBoardViewPlayerImageGetter {
    fun getImageView(playerNum: Int): ImageView
}