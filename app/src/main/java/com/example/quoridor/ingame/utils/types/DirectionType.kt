package com.example.quoridor.ingame.utils.types

import com.example.quoridor.ingame.utils.Coordinate

enum class DirectionType(val diff: Coordinate) {
    UP(Coordinate(-1, 0)),
    DOWN(Coordinate(+1, 0)),
    RIGHT(Coordinate(0, +1)),
    LEFT(Coordinate(0, -1))
}