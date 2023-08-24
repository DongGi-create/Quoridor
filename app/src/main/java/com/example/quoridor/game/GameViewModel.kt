package com.example.quoridor.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quoridor.customView.gameBoardView.Board
import com.example.quoridor.customView.gameBoardView.Wall
import com.example.quoridor.customView.playerView.Player
import com.example.quoridor.game.util.GameFunc
import com.example.quoridor.util.Coordinate
import com.example.quoridor.util.Func.set

class GameViewModel: ViewModel() {

    private val TAG = "Dirtfy Test - GameViewModel"

    val board: MutableLiveData<Board> by lazy {
        MutableLiveData<Board>()
    }
    val players: Array<MutableLiveData<Player>> by lazy {
        arrayOf( MutableLiveData<Player>(), MutableLiveData<Player>() )
    }
    val availableMoves: MutableLiveData<Array<Coordinate>> by lazy {
        MutableLiveData<Array<Coordinate>>()
    }
    val turn: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val isEnd: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun wallCross(wall: Wall): Boolean {
        return GameFunc.wallCross(wall, board.value!!)
    }
    fun wallClosed(wall: Wall): Boolean {
        Log.d(TAG, "wallClosed")
        return GameFunc.wallCross(wall, board.value!!)
    }
    fun wallMatch(wall: Wall): Boolean {
        Log.d(TAG, "wallMatch")
        return GameFunc.wallMatch(wall, board.value!!)
    }
    fun isAvailableMove(coordinate: Coordinate): Boolean {
        Log.d(TAG, "isAvailableMove")
        return coordinate in availableMoves.value!!
    }
    fun setAvailableMove(available: Array<Coordinate>) {
        Log.d(TAG, "setAvailableMove")
        availableMoves.value = available
    }
    fun getAvailableMoves(turn: Int) {
        Log.d(TAG, "getAvailableMoves($turn)")
        availableMoves.value = GameFunc.getAvailableMove(
            turn,
            board.value!!
        )
    }
    fun getAvailableMoves() {
        Log.d(TAG, "getAvailableMoves()")
        availableMoves.value = GameFunc.getAvailableMove(
            turn.value!!,
            board.value!!
        )
    }
    fun isPlayer(coordinate: Coordinate): Boolean {
        Log.d(TAG, "isPlayer")
        return board.value!!.playCoordinates[0] == coordinate ||
                board.value!!.playCoordinates[1] == coordinate
    }

    fun move(coordinate: Coordinate) {
        Log.d(TAG, "move")
        val turnValue = turn.value!!
        val boardValue = board.value!!

        boardValue.playCoordinates[turnValue] = coordinate

        board.value = boardValue

        if (GameFunc.reachedEnd(coordinate, turnValue))
            isEnd.value = true
    }
    fun addVerticalWall(coordinate: Coordinate) {
        Log.d(TAG, "addVerticalWall")
        val turnValue = turn.value!!
        val playerValue = players[turnValue].value!!
        val boardValue = board.value!!

        boardValue.verticalWalls.set(coordinate, true)
        playerValue.leftWall -= 1

        players[turnValue].value = playerValue
        board.value = boardValue
    }
    fun addHorizontalWall(coordinate: Coordinate) {
        Log.d(TAG, "addHorizontalWall")
        val turnValue = turn.value!!
        val playerValue = players[turnValue].value!!
        val boardValue = board.value!!

        boardValue.horizontalWalls.set(coordinate, true)
        playerValue.leftWall -= 1

        players[turnValue].value = playerValue
        board.value = boardValue
    }
    fun turnPass() {
        Log.d(TAG, "turnPass")
        val turnValue = turn.value!!
        turn.value = (turnValue+1)%2
    }

    fun isMyTurn(myTurn: Int): Boolean {
        Log.d(TAG, "isMyTurn")
        val turnValue = turn.value!!
        return turnValue == myTurn
    }
    fun isWallLeft(turn: Int): Boolean {
        Log.d(TAG, "isWallLeft($turn)")
        return players[turn].value!!.leftWall > 0
    }
    fun isWallLeft(): Boolean {
        Log.d(TAG, "isWallLeft()")
        return players[turn.value!!].value!!.leftWall > 0
    }
    fun getPlayerValue(turn: Int): Player {
        Log.d(TAG, "getPlayerValues")
        return players[turn].value!!
    }
    fun getTurn(): Int {
        Log.d(TAG, "getTurn")
        return turn.value!!
    }

}
