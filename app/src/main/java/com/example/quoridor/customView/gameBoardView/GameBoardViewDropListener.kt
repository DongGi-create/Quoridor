package com.example.quoridor.customView.gameBoardView

import android.view.View
import com.example.quoridor.game.util.types.DropReturnType
import com.example.quoridor.game.util.types.WallType

interface GameBoardViewDropListener {
    fun drop(matchedView: View, wallType: WallType, row: Int, col: Int): DropReturnType
}