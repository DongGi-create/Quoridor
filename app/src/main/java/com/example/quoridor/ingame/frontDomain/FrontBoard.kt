package com.example.quoridor.ingame.frontDomain

import android.util.Pair
import com.example.quoridor.domain.Board
import com.example.quoridor.domain.Notation
import com.example.quoridor.domain.utils.NotationType
import com.example.quoridor.ingame.utils.WallType

class FrontBoard(
    val players: Array<FrontPlayer>,
    verticalWalls: ArrayList<Pair<Int, Int>>,
    horizontalWalls: ArrayList<Pair<Int, Int>>,
    is_1vs1: Boolean
): Board(players, verticalWalls, horizontalWalls, is_1vs1) {

    private var turn = 0
    val walls = Array(2){Array(8){Array(8){false}}}

    private fun move(row: Int, col: Int){
        players[turn].row = row
        players[turn].col = col
    }
    private fun addVerticalWall(row: Int, col: Int){
        players[turn].left_wall -= 1
        verticalWalls.add(Pair(row, col))
        walls[WallType.Vertical.ordinal][row][col] = true
    }
    private fun addHorizontalWall(row: Int, col: Int){
        players[turn].left_wall -= 1
        horizontalWalls.add(Pair(row, col))
        walls[WallType.Horizontal.ordinal][row][col] = true
    }

    fun doNotation(notation: Notation){
        players[turn].turnEnd()

        when(notation.type){
            NotationType.PLAYER -> move(notation.row, notation.col)
            NotationType.VERTICAL -> addVerticalWall(notation.row, notation.col)
            NotationType.HORIZONTAL -> addHorizontalWall(notation.row, notation.col)
            null -> { }
        }

        turn += 1
        turn %= if (isIs_1v1) 2 else 4

        players[turn].turnStart()
    }

    fun nowPlayer(): FrontPlayer {
        return players[turn]
    }

    fun start(){
        players[0].turnStart()
    }
}