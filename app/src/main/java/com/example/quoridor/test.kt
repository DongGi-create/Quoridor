package com.example.quoridor

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.quoridor.databinding.ActivityTestBinding

class test : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        val binding = ActivityTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
