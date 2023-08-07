package com.example.quoridor.ingame.utils

enum class DirectionType(val diff: Coordinate) {
    UP(Coordinate(-1, 0)),
    DOWN(Coordinate(+1, 0)),
    RIGHT(Coordinate(0, +1)),
    LEFT(Coordinate(0, -1))
}