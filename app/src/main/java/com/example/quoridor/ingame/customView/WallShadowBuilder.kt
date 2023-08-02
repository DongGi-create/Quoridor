package com.example.quoridor.ingame.customView

import android.graphics.Point
import android.view.View

class WallShadowBuilder(v: View): View.DragShadowBuilder(v) {
    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        val width = view.width
        val height = view.height

        outShadowSize!!.set(width, height)
        outShadowTouchPoint!!.set(width+50, height+50)

    }
}