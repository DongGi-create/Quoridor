package com.example.quoridor.util

import androidx.appcompat.app.AppCompatActivity

abstract class NonStackActivity: AppCompatActivity() {

    override fun onStop() {
        super.onStop()
        finish()
    }
}