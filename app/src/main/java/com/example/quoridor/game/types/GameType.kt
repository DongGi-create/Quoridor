package com.example.quoridor.game.types

enum class GameType(val timeLimit: Long) {
    BLITZ(1000*60*3),
    STANDARD(1000*60*10),
    CLASSIC(1000*60*30)
}