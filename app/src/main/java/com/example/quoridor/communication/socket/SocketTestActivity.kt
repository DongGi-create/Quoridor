package com.example.quoridor.communication.socket

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.R
import com.example.quoridor.communication.retrofit.util.ToastHttpResult
import com.example.quoridor.communication.socket.WebSocketTest.client
import com.example.quoridor.databinding.ActivitySocketTestBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.WebSocket


class SocketTestActivity: AppCompatActivity() {

    companion object {
        val URL = WebSocketTest.BASE_URL +"game/move"+"?gameId="+ ToastHttpResult.data1.gameId+"&turn="+ ToastHttpResult.data1.turn.toString();
//        const val URL = WebSocketTest.BASE_URL+"game/move"

        val request = Request.Builder().url(URL).build();
        val listener = WebSocketListenerTest()

        lateinit var ws: WebSocket
    }

    private var byteArray = byteArrayOf()

    private lateinit var jsonData :String
    private val binding by lazy {
        ActivitySocketTestBinding.inflate(layoutInflater)
    }

    private val TAG by lazy {
        getString(R.string.Dirtfy_test_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.connect.setOnClickListener {
            buildNetworkJob(
                buildAsyncJob {
                    ws = client.newWebSocket(request, listener)
                    Log.d(TAG, "web socket creating")
                }
            ) {
                Log.d(TAG, "web socket created")
                Toast.makeText(this@SocketTestActivity, "web socket created", Toast.LENGTH_SHORT)
                    .show()
            }.start()
        }

        binding.makeMassage.setOnClickListener {
            val data = DTO.Data(1000 * 60 * 3L, 0, 0, 3)
            jsonData = Gson().toJson(data)
//            val data = 'c'
//            Log.d(TAG, "${data.toByteArray().size}\n${data.toByteArray().toHexString()}")
//            for (b in data.toByteArray()) {
//                Log.d(TAG, "${b}")
//            }
//            Log.d(TAG, 'a'.code.toByte().toString())
//            Log.d(TAG, 'b'.code.toByte().toString())
//            Log.d(TAG, 'c'.code.toByte().toString())
//            Log.d(TAG, fromByteArray<Char>(data.toByteArray()).toString())
            /*byteArray = data.toByteArray()
            Log.d(TAG, byteArray.toHexString())
            Log.d(TAG, byteArray.toData()?.toString()?:"error")*/
        }

        binding.sendMassage.setOnClickListener {
            val data = DTO.Data(1000 * 60 * 3L, 0, 0, 3)
            jsonData = Gson().toJson(data)
            buildAsyncJob {
                ws.send("$data");
            }.start()
        }

        binding.close.setOnClickListener {
            buildAsyncJob {
                ws.close(1000, "close btn")
            }.start()
        }
    }

    override fun onPause() {
        super.onPause()
        ws.close(1000, "end")
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
                Log.d(TAG, "networkJob start")
                networkJob.await()
                withContext(Dispatchers.Main) {
                    afterJob()
                }
            }
    }

}