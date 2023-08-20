package com.example.quoridor.socket

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.R
import com.example.quoridor.databinding.ActivitySocketTestBinding
import com.example.quoridor.socket.Parser.toByteArray
import com.example.quoridor.socket.Parser.toData
import com.example.quoridor.socket.Parser.toHexString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket

class SocketTestActivity: AppCompatActivity() {

    companion object {
        lateinit var socket: Socket
        lateinit var inputStream: InputStream
        lateinit var outputStream: OutputStream
    }

    private var byteArray = byteArrayOf()

    private val binding by lazy {
        ActivitySocketTestBinding.inflate(layoutInflater)
    }

    private val TAG by lazy {
        getString(R.string.Dirtfy_test_tag)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        buildNetworkJob(
            buildAsyncJob {
                socket = Socket("43.201.189.249", 8080)
                inputStream = socket.getInputStream()
                outputStream = socket.getOutputStream()
            }
        ) {
            Log.d(TAG, "socket created")
            Toast.makeText(this@SocketTestActivity, "socket created", Toast.LENGTH_SHORT).show()
        }.start()

        binding.makeMassage.setOnClickListener {
            val data = DTO.Data('n', 1000*60*3L, 'm', '0', '3')
//            val data = 'c'
//            Log.d(TAG, "${data.toByteArray().size}\n${data.toByteArray().toHexString()}")
//            for (b in data.toByteArray()) {
//                Log.d(TAG, "${b}")
//            }
//            Log.d(TAG, 'a'.code.toByte().toString())
//            Log.d(TAG, 'b'.code.toByte().toString())
//            Log.d(TAG, 'c'.code.toByte().toString())
//            Log.d(TAG, fromByteArray<Char>(data.toByteArray()).toString())

            byteArray = data.toByteArray()

            Log.d(TAG, byteArray.toHexString())
            Log.d(TAG, byteArray.toData()?.toString()?:"error")
        }

        binding.sendMassage.setOnClickListener {
            buildAsyncJob {
                outputStream.write(byteArray)
            }.start()
        }

        binding.readMassage.setOnClickListener {

        }
    }

    private fun buildAsyncJob(networkJob: () -> Unit): Deferred<Unit> {
        return CoroutineScope(Dispatchers.IO)
            .async(start = CoroutineStart.LAZY) {
                networkJob()
            }
    }
    private fun buildNetworkJob(networkJob: Deferred<Unit>, afterJob: () -> Unit = {}): Job {
        return CoroutineScope(Dispatchers.IO)
            .launch(start = CoroutineStart.LAZY) {
                networkJob.start()
                networkJob.await()
                withContext(Dispatchers.Main) {
                    afterJob()
                }
            }
    }



}