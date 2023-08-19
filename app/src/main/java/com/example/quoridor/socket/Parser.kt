package com.example.quoridor.socket

import android.util.Log
import com.example.quoridor.socket.Parser.toData
import com.example.quoridor.socket.Parser.toLong
import okhttp3.internal.toHexString
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInput
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.nio.ByteBuffer
import java.util.BitSet

object Parser {

    private val TAG = "Dirtfy Test"

    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> fromByteArray(byteArray: ByteArray): T {
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        val objectInput: ObjectInput
        objectInput = ObjectInputStream(byteArrayInputStream)
        val result = objectInput.readObject() as T
        objectInput.close()
        byteArrayInputStream.close()
        return result
    }

    fun Serializable.toByteArray(): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream: ObjectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.flush()
        val result = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.close()
        objectOutputStream.close()
        return result
    }

    fun Long.toByteArray(): ByteArray {
        val buffer: ByteBuffer = ByteBuffer.allocate(java.lang.Long.BYTES)
        buffer.putLong(this)
        return buffer.array()
    }


    fun ByteArray.toHexString() = joinToString(" ") { "0x%02X".format(it) }

    fun DTO.Data.toByteArray(): ByteArray {
        var ba = byteArrayOf()
        ba += this.isEnd.code.toByte()
        ba += this.remainTime.toByteArray()
        ba += this.type.code.toByte()
        ba += this.row.code.toByte()
        ba += this.col.code.toByte()
        return ba
    }

    fun ByteArray.toData(): DTO.Data? {
        return if (this.size != 12) null
        else {
            val isEnd = this[0].toInt().toChar()
            val remain = this.toLong(1)
            val type = this[9].toInt().toChar()
            val row = this[10].toInt().toChar()
            val col = this[11].toInt().toChar()

            DTO.Data(isEnd, remain, type, row, col)
        }
    }

    fun ByteArray.toLong(start: Int = 0): Long {
        var result = 0L
        for (i in start until start+8) {
            result = result or ((this[i].toLong() and 255L) shl 8 * (7-(i-start)))
            Log.d(TAG, "$i ${(this[i].toLong() and 255L).toByteArray().toHexString()}")
            Log.d(TAG, "$i ${result.toByteArray().toHexString()}")
        }
        return result
    }
}