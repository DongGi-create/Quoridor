package com.example.quoridor.ingame.customView.gameBoardView

import android.view.View
import com.example.quoridor.ingame.utils.types.DropReturnType
import com.example.quoridor.ingame.utils.types.WallType

interface GameBoardViewDropListener {
    fun drop(matchedView: View, wallType: WallType, row: Int, col: Int): DropReturnType
}