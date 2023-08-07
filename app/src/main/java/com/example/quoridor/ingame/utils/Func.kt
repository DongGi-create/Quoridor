package com.example.quoridor.ingame.utils

import android.util.Log
import com.example.quoridor.domain.Notation
import com.example.quoridor.domain.utils.NotationType
import com.example.quoridor.ingame.frontDomain.FrontBoard
import kotlin.math.pow
import kotlin.math.sqrt

class Func {
    companion object {
        fun distance(p1: Pair<Float, Float>, p2: Pair<Float, Float>): Float{
            return sqrt((p1.first-p2.first).pow(2) + (p1.second-p2.second).pow(2))
        }

        fun <T> Array<Array<T>>.get(cor: Coordinate): T {
            return this[cor.r][cor.c]
        }
        fun <T> Array<Array<T>>.set(cor: Coordinate, value: T) {
            this[cor.r][cor.c] = value
        }

        fun wallCross(notation: Notation, board: FrontBoard): Boolean {
            val row = notation.row
            val col = notation.col
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

        fun wallClosed(notation: Notation, board: FrontBoard): Boolean {
            val flag = arrayOf(true, true, true, true)
            val len = board.players.size
            val wallType = when(notation.type){
                NotationType.HORIZONTAL -> WallType.Horizontal
                else -> WallType.Vertical
            }

            board.walls[wallType.ordinal][notation.row][notation.col] = true

            for (d in DirectionType.values()) {
                val i = d.ordinal

                if (i >= len)
                    break

                val p = board.players[i]
                val row = p.row
                val col = p.col
                val cor = Coordinate(row, col)

                val reached = BFS(cor, board.walls)
                Log.d("Dirtfy","${i} ")
                for(r in reached){
                    Log.d("Dirtfy","${r.r} ${r.c}")
                }
                for (r in reached) {
                    when(d) {
                        DirectionType.UP -> {
                            if (r.r == 0) {
                                flag[i] = false
                                break
                            }
                        }
                        DirectionType.RIGHT -> {
                            if (r.c == 8) {
                                flag[i] = false
                                break
                            }
                        }
                        DirectionType.DOWN -> {
                            if (r.r == 8) {
                                flag[i] = false
                                break
                            }
                        }
                        DirectionType.LEFT -> {
                            if (r.c == 0) {
                                flag[i] = false
                                break
                            }
                        }
                    }
                }
            }

            board.walls[wallType.ordinal][notation.row][notation.col] = false

            for (i in 0 .. 3){
                if (i >= len) break
                if (!flag[i]) continue
                return true
            }
            return false
        }

        fun getAvailableMove(me: Int, board: FrontBoard): Array<Coordinate> {
            var available = Array(0) { Coordinate(0, 0) }

            val row = board.players[me].row
            val col = board.players[me].col
            val now = Coordinate(row, col)
            for (d in DirectionType.values()) {
                val next1 = now + d.diff

                // wall test
                if (!canMove(now, next1, d, board.walls)) continue

                // first player test
                var next1IsPlayer = false

                for (p in board.players){
                    if (p.row == next1.r && p.col == next1.c)
                        next1IsPlayer = true
                }

                if (next1IsPlayer) {
                    val next2 = next1 + d.diff

                    // wall test
                    if (!canMove(next1, next2, d, board.walls)) continue
                    // 범위 밖이어서 못가는 경우!! 벽을 놔두어서 못가는 경우로 함수 두개로 나누어서 생각

                    // second player test
                    var next2IsPlayer = false

                    for (p in board.players){
                        if (p.row == next2.r && p.col == next2.c)
                            next2IsPlayer = true
                    }

                    if (next2IsPlayer) continue

                    available += next2
                }
                else {
                    available += next1
                }
            }

            return available
        }
    }
}