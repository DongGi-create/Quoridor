package com.example.quoridor.game.types

import java.io.Serializable

enum class GameType(val initWall: Int, val timeLimit: Long): Serializable {
    BLITZ(10, 1000*60*3),
    STANDARD(10, 1000*60*10),
    CLASSIC(10, 1000*60*30)
}