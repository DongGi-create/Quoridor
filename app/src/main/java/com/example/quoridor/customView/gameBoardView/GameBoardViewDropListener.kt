package com.example.quoridor.customView.gameBoardView

import android.view.View
import com.example.quoridor.game.util.types.DropReturnType
import com.example.quoridor.game.util.types.WallType

interface GameBoardViewDropListener {
    fun cross(matchedView: View, wall: Wall)
    fun closed(matchedView: View, wall: Wall)
    fun match(matchedView: View, wall: Wall)
    fun success(matchedView: View, wall: Wall)
}