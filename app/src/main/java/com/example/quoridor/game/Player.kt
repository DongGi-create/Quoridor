package com.example.quoridor.game

import com.example.quoridor.util.Coordinate
import com.example.quoridor.game.util.types.NotationType

abstract class Player(
    val coordinate: Coordinate,
    var leftWall: Int,
) {
    abstract fun turnStart()
    fun play(notation: Notation) {
        when(notation.type){
            NotationType.MOVE -> {
                coordinate += notation.coordinate
            }
            NotationType.VERTICAL, NotationType.HORIZONTAL -> leftWall--
        }
    }
    abstract fun turnEnd()
}
