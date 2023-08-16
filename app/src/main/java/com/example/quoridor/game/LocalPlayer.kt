package com.example.quoridor.game

import android.widget.ImageView
import com.example.quoridor.util.Coordinate
import com.example.quoridor.game.util.timeCounter.TimeCounter

class LocalPlayer(
    val image: ImageView,
    val timer: TimeCounter,
    coordinate: Coordinate,
    leftWall: Int
): Player(coordinate, leftWall) {

    override fun turnStart() {
        timer.start()
    }

    override fun turnEnd() {
        timer.pause()
    }

}