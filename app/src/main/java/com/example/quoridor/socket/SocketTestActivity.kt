package com.example.quoridor.socket

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivitySocketTestBinding
import java.net.Socket

class SocketTestActivity: AppCompatActivity() {

    companion object {
        val socket = Socket("43.201.189.249", 8080)
        val inputStream = socket.getInputStream()
        val outputStream = socket.getOutputStream()
    }

    private val binding by lazy {
        ActivitySocketTestBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


//        val byteArray = inputStream.readNBytes(3)

        socket.connect(socket.remoteSocketAddress, 1000)
    }
}