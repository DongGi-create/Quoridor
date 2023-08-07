package com.example.quoridor.ingame.utils

data class Coordinate(val r: Int, val c: Int) {
    operator fun plus(other: Coordinate): Coordinate {
        return Coordinate(this.r+other.r, this.c+other.c)
    }
}