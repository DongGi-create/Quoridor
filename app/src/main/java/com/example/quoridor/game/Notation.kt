package com.example.quoridor.game

import com.example.quoridor.util.Coordinate
import com.example.quoridor.game.util.types.NotationType

data class Notation(
    val type: NotationType,
    val coordinate: Coordinate
)
