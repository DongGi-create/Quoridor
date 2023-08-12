package com.example.quoridor.ingame.customView.gameBoardView

import android.view.View

interface GameBoardViewPieceClickListener {
    fun click(clickedPiece: View, row: Int, col: Int)
}