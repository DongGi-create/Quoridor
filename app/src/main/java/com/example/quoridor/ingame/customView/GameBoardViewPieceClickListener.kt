package com.example.quoridor.ingame.customView

import android.view.View

interface GameBoardViewPieceClickListener {
    fun click(clickedPiece: View, row: Int, col: Int)
}