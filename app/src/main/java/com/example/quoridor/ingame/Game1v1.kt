package com.example.quoridor.ingame

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.example.quoridor.databinding.ActivityGame1v1Binding

class Game1v1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityGame1v1Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}