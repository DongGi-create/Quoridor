package com.example.quoridor.util

data class Coordinate(var r: Int, var c: Int) {
    operator fun plus(other: Coordinate): Coordinate {
        return Coordinate(this.r+other.r, this.c+other.c)
    }
    operator fun plusAssign(other: Coordinate) {
        this.r += other.r
        this.c += other.c
    }
    override operator fun equals(other: Any?): Boolean {
        return when(other) {
            is Coordinate -> {
                (r == other.r) && (c == other.c)
            }
            else -> false
        }
    }
}