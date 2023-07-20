package com.example.quoridor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.quoridor.databinding.ActivityMainBinding
import com.example.quoridor.ingame.Game1v1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGamestart.setOnClickListener {
            val intent = Intent(this, Game1v1::class.java)
            startActivity(intent)
        }
    }
}