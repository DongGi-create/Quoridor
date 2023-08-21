package com.example.quoridor.customView.gameBoardView

import android.view.View

sealed interface GameBoardViewWallListener {

    interface DragListener {
        fun startDrag(): Boolean
    }

    interface DropListener {
        fun cross(matchedView: View, wall: Wall): Boolean
        fun closed(matchedView: View, wall: Wall): Boolean
        fun match(matchedView: View, wall: Wall): Boolean
        fun success(matchedView: View, wall: Wall)
    }

}