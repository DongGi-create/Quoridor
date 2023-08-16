package com.example.quoridor.game

import com.example.quoridor.util.Coordinate
import com.example.quoridor.game.util.GameFunc.set
import com.example.quoridor.game.util.types.NotationType
import com.example.quoridor.game.util.types.WallType

class Board(
    val playerCorList: Array<Coordinate>,
    val walls: Array<Array<Array<Boolean>>>
) {

    private fun move(next: Coordinate, referee: Referee){
        playerCorList[referee.turn] = next
    }
    private fun addVerticalWall(coordinate: Coordinate){
        walls[WallType.Vertical.ordinal].set(coordinate, true)
    }
    private fun addHorizontalWall(coordinate: Coordinate){
        walls[WallType.Horizontal.ordinal].set(coordinate, true)
    }

    fun doNotation(notation: Notation, referee: Referee){

        val coordinate = notation.coordinate

        referee.playerList[referee.turn].play(notation)

        when(notation.type){
            NotationType.MOVE -> move(coordinate, referee)
            NotationType.VERTICAL -> addVerticalWall(coordinate)
            NotationType.HORIZONTAL -> addHorizontalWall(coordinate)
        }

    }
}