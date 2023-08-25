package com.example.quoridor.customView.playerView

data class Player(
    val name: String,
    var leftTime: Long,
    var leftWall: Int,
    val rating: Int,
    var myTurn: Boolean
)