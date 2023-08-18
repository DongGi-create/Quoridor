package com.example.quoridor.customView.gameBoardView

import android.widget.ImageView
import com.example.quoridor.util.Coordinate

data class Board(
    val playerImageViews: Array<ImageView>,
    val playCoordinates: Array<Coordinate> = arrayOf(Coordinate(8, 4), Coordinate(0, 4)),
    val verticalWalls: Array<Array<Boolean>> = Array(8) { Array(8) { false } },
    val horizontalWalls: Array<Array<Boolean>> = Array(8) { Array(8) { false } }
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (!playCoordinates.contentEquals(other.playCoordinates)) return false
        if (!verticalWalls.contentDeepEquals(other.verticalWalls)) return false
        if (!horizontalWalls.contentDeepEquals(other.horizontalWalls)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playCoordinates.contentHashCode()
        result = 31 * result + verticalWalls.contentDeepHashCode()
        result = 31 * result + horizontalWalls.contentDeepHashCode()
        return result
    }
}