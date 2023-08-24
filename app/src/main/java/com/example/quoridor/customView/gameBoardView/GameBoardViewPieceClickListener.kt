package com.example.quoridor.customView.gameBoardView

import android.view.View
import com.example.quoridor.util.Coordinate

interface GameBoardViewPieceClickListener {
    fun click(clickedPiece: View, coordinate: Coordinate)
}