package com.example.quoridor.customView.gameBoardView

import com.example.quoridor.util.Coordinate

data class Wall(
    val type: WallType,
    val coordinate: Coordinate
)
