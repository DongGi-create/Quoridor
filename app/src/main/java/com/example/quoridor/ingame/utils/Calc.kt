package com.example.quoridor.ingame.utils

import kotlin.math.pow
import kotlin.math.sqrt

class Calc {
    companion object {
        fun distance(p1: Pair<Float, Float>, p2: Pair<Float, Float>): Float{
            return sqrt((p1.first-p2.first).pow(2) + (p1.second-p2.second).pow(2))
        }
    }
}