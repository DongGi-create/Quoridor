package com.example.quoridor.game

interface Referee {
    var turn: Int
    val playerList: Array<Player>

    fun initGame()
    fun turnPass(notation: Notation)
}