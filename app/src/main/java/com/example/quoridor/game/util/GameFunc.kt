package com.example.quoridor.game.util

import com.example.quoridor.game.util.types.DirectionType
import com.example.quoridor.game.util.types.GameResultType
import com.example.quoridor.game.util.types.WallType
import com.example.quoridor.game.Board
import com.example.quoridor.game.Notation
import com.example.quoridor.game.util.types.NotationType
import com.example.quoridor.util.Coordinate
import kotlin.math.pow

object GameFunc {

    private val ratio = 10.0
    private val diff = 400.0
    private val K = 20.0

    fun <T> Array<Array<T>>.get(cor: Coordinate): T {
        return this[cor.r][cor.c]
    }
    fun <T> Array<Array<T>>.set(cor: Coordinate, value: T) {
        this[cor.r][cor.c] = value
    }

    private fun calcW(myRating: Int, opponentRating: Int): Double {
        return 1/(ratio.pow((myRating - opponentRating) / diff) + 1)
    }

    fun calcRating(myRating: Int, opponentRating: Int, gameResult: GameResultType): Int {
        return (myRating + K * (gameResult.ordinal/2.0 - calcW(myRating, opponentRating))).toInt()
    }

    fun wallCross(notation: Notation, board: Board): Boolean {
        val (row, col) = notation.coordinate

        var ret = false

        when(notation.type){
            NotationType.VERTICAL -> {
                if(row > 0) ret = ret || board.walls[WallType.Vertical.ordinal][row-1][col]
                if(row < 7) ret = ret || board.walls[WallType.Vertical.ordinal][row+1][col]
                ret = ret || board.walls[WallType.Vertical.ordinal][row][col]
                ret = ret || board.walls[WallType.Horizontal.ordinal][row][col]
            }
            NotationType.HORIZONTAL -> {
                if (col > 0) ret = ret || board.walls[WallType.Horizontal.ordinal][row][col - 1]
                if (col < 7) ret = ret || board.walls[WallType.Horizontal.ordinal][row][col + 1]
                ret = ret || board.walls[WallType.Horizontal.ordinal][row][col]
                ret = ret || board.walls[WallType.Vertical.ordinal][row][col]
            }
            else -> { ret = true }
        }

        return ret
    }

    fun canMove(fromCor: Coordinate, toCor: Coordinate, dir: DirectionType, walls: Array<Array<Array<Boolean>>>): Boolean{
        val (fr, fc) = fromCor
        val (tr, tc) = toCor

        if (tr > 8 || tr < 0 || tc > 8 || tc < 0) return false

        val horiWalls = walls[WallType.Horizontal.ordinal]
        val vertWalls = walls[WallType.Vertical.ordinal]

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

    fun wallClosed(notation: Notation, board: Board): Boolean {
        val flag = arrayOf(true, true, true, true)
        val len = board.playerCorList.size
        val wallType = when(notation.type){
            NotationType.HORIZONTAL -> WallType.Horizontal
            else -> WallType.Vertical
        }

        board.walls[wallType.ordinal].set(notation.coordinate, true)

        for (d in DirectionType.values()) {
            val i = d.ordinal

            if (i >= len)
                break

            val cor = board.playerCorList[i]

            val reached = BFS(cor, board.walls)
            for (r in reached) {
                if (reachedEnd(r, i)){
                    flag[i] = false
                    break
                }
            }
        }

        board.walls[wallType.ordinal].set(notation.coordinate, false)

        for (i in 0 .. 3){
            if (i >= len) break
            if (!flag[i]) continue
            return true
        }
        return false
    }

    fun getAvailableMove(me: Int, board: Board): Array<Coordinate> {

        val source = mutableListOf<Coordinate>()
        val available = mutableListOf<Coordinate>()

        val now = board.playerCorList[me]

        source.add(now)

        for (d in DirectionType.values()) {
            val next = now + d.diff

            if (!canMove(now, next, d, board.walls)) continue

            // first player test
            var isPlayer = false

            for (p in board.playerCorList){
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

                if (!canMove(s, next, d, board.walls)) continue

                // first player test
                var isPlayer = false

                for (p in board.playerCorList){
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