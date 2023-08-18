package com.example.quoridor.game.util

import com.example.quoridor.customView.gameBoardView.Board
import com.example.quoridor.customView.gameBoardView.Wall
import com.example.quoridor.customView.gameBoardView.WallType
import com.example.quoridor.game.types.DirectionType
import com.example.quoridor.game.types.GameResultType
import com.example.quoridor.util.Coordinate
import com.example.quoridor.util.Func.get
import com.example.quoridor.util.Func.set
import kotlin.math.pow

object GameFunc {

    private val ratio = 10.0
    private val diff = 400.0
    private val K = 20.0

    private fun calcW(myRating: Int, opponentRating: Int): Double {
        return 1/(ratio.pow((myRating - opponentRating) / diff) + 1)
    }

    fun calcRating(myRating: Int, opponentRating: Int, gameResult: GameResultType): Int {
        return (myRating + K * (gameResult.ordinal/2.0 - calcW(myRating, opponentRating))).toInt()
    }

    fun wallCross(wall: Wall, board: Board): Boolean {
        val (row, col) = wall.coordinate

        var ret = false

        when(wall.type){
            com.example.quoridor.customView.gameBoardView.WallType.VERTICAL -> {
                if(row > 0) ret = ret || board.verticalWalls[row-1][col]
                if(row < 7) ret = ret || board.verticalWalls[row+1][col]
                ret = ret || board.horizontalWalls[row][col]
            }
            com.example.quoridor.customView.gameBoardView.WallType.HORIZONTAL -> {
                if (col > 0) ret = ret || board.horizontalWalls[row][col - 1]
                if (col < 7) ret = ret || board.horizontalWalls[row][col + 1]
                ret = ret || board.verticalWalls[row][col]
            }
        }

        return ret
    }

    fun wallClosed(wall: Wall, board: Board): Boolean {
        val flag = arrayOf(true, true, true, true)
        val len = board.playCoordinates.size

        val tmpWall = when(wall.type) {
            com.example.quoridor.customView.gameBoardView.WallType.VERTICAL -> board.verticalWalls
            com.example.quoridor.customView.gameBoardView.WallType.HORIZONTAL -> board.horizontalWalls
        }
        val walls = arrayOf(board.verticalWalls, board.horizontalWalls)

        tmpWall.set(wall.coordinate, true)

        for (d in DirectionType.values()) {
            val i = d.ordinal

            if (i >= len)
                break

            val cor = board.playCoordinates[i]

            val reached = BFS(cor, walls)
            for (r in reached) {
                if (reachedEnd(r, i)){
                    flag[i] = false
                    break
                }
            }
        }

        tmpWall.set(wall.coordinate, false)

        for (i in 0 .. 3){
            if (i >= len) break
            if (!flag[i]) continue
            return true
        }
        return false
    }

    fun wallMatch(wall: Wall, board: Board): Boolean {
        val (row, col) = wall.coordinate
        return when(wall.type){
            com.example.quoridor.customView.gameBoardView.WallType.VERTICAL -> {
                board.verticalWalls[row][col]
            }
            com.example.quoridor.customView.gameBoardView.WallType.HORIZONTAL -> {
                board.horizontalWalls[row][col]
            }
        }
    }

    fun canMove(fromCor: Coordinate, toCor: Coordinate, dir: DirectionType, walls: Array<Array<Array<Boolean>>>): Boolean{
        val (fr, fc) = fromCor
        val (tr, tc) = toCor

        if (tr > 8 || tr < 0 || tc > 8 || tc < 0) return false

        val horiWalls = walls[WallType.HORIZONTAL.ordinal]
        val vertWalls = walls[WallType.VERTICAL.ordinal]

        return when(dir){
            DirectionType.DOWN -> { // 아래
                !( (fc < 8 && horiWalls[fr][fc]) || (fc > 0 && horiWalls[fr][fc-1]) )
            }
            DirectionType.RIGHT -> { // 오른쪽
                !( (fr < 8 && vertWalls[fr][fc]) || (fr > 0 && vertWalls[fr-1][fc]) )
            }
            DirectionType.UP -> { // 위
                !( (fc < 8 && horiWalls[fr-1][fc]) || (fc > 0 && horiWalls[fr-1][fc-1]) )
            }
            DirectionType.LEFT -> { // 왼쪽
                !( (fr < 8 && vertWalls[fr][fc-1]) || (fr > 0 && vertWalls[fr-1][fc-1]) )
            }
        }
    }
    fun BFS(start: Coordinate, walls: Array<Array<Array<Boolean>>>): Array<Coordinate> {
        val queue = Array(81){ Coordinate(-1, -1) }
        var front = 0
        var back = -1

        val visit = Array(9){ Array(9){false} }

        queue[++back] = start
        visit.set(start, true)
        while( front <= back ) {
            val now = queue[front++]

            for (d in DirectionType.values()) {
                val next = now + d.diff

                if (!canMove(now, next, d, walls)) continue
                if (visit.get(next)) continue

                queue[++back] = next
                visit.set(next, true)
            }
        }

        return queue
    }

    fun reachedEnd(cor: Coordinate, turn: Int): Boolean {
        return when(turn){
            DirectionType.UP.ordinal ->{
                cor.r == 0
            }
            DirectionType.DOWN.ordinal -> {
                cor.r == 8
            }
            DirectionType.RIGHT.ordinal -> {
                cor.c == 8
            }
            DirectionType.LEFT.ordinal -> {
                cor.c == 0
            }

            else -> { false }
        }
    }

    fun getAvailableMove(me: Int, board: Board): Array<Coordinate> {

        val source = mutableListOf<Coordinate>()
        val available = mutableListOf<Coordinate>()

        val now = board.playCoordinates[me]
        val walls = arrayOf(board.verticalWalls, board.horizontalWalls)

        source.add(now)

        for (d in DirectionType.values()) {
            val next = now + d.diff

            if (!canMove(now, next, d, walls)) continue

            // first player test
            var isPlayer = false

            for (p in board.playCoordinates){
                if (p == next)
                    isPlayer = true
            }

            if (isPlayer)//IsPlayer있고 뒤에 벽이 있다면
                source.add(next)
        }

        for (s in source) {
            //isPlayer 그 뒤에 벽이 없을때만 다르게
            for (d in DirectionType.values()) {
                val next = s + d.diff

                if (!canMove(s, next, d, walls)) continue

                // first player test
                var isPlayer = false

                for (p in board.playCoordinates){
                    if (p == next)
                        isPlayer = true
                }

                if (!isPlayer)
                    available.add(next)
            }
        }

        val set = available.toMutableSet()

        set.removeAll(source.toSet())

        return set.toTypedArray()
    }
}