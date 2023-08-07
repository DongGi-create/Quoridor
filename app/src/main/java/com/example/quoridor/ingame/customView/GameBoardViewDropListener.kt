package com.example.quoridor.ingame.customView

import android.view.View
import com.example.quoridor.ingame.utils.DropReturnType
import com.example.quoridor.ingame.utils.WallType

interface GameBoardViewDropListener {
    fun drop(matchedView: View, wallType: WallType, row: Int, col: Int): DropReturnType
}