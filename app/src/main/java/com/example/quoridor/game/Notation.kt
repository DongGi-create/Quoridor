package com.example.quoridor.game

import com.example.quoridor.game.types.NotationType
import com.example.quoridor.util.Coordinate

data class Notation(
    val type: NotationType,
    val coordinate: Coordinate
)
