package io.github.pdkst.bilibili.danmu

import java.nio.ByteBuffer

class DanmuPack(val dataBuffer: ByteBuffer, offset: Int = 0) {

    val packetLength: Int
    val headerLength: Short
    val version: Short
    val operatoion: Int
    val sequence: Int
    val value: ByteBuffer

    companion object Setting {
        const val rawHeaderLen = 16
        const val packetOffset = 0
        const val headerOffset = 4
        const val versionOffset = 6
        const val operationOffset = 8
        const val seqOffset = 12

        fun packString(msg: String?, operation: Int = Operation.HEARTBEAT): DanmuPack {
            val msgBytes = msg?.byteInputStream()?.readBytes()
            return msgBytes?.let {
                packBuffer(it, operation)
            } ?: packBuffer(msgBytes, Operation.HEARTBEAT)
        }

        private fun packBuffer(msgBytes: ByteArray?, operation: Int): DanmuPack {
            val bodyLength: Int = msgBytes?.size ?: 0
            val rawLength = rawHeaderLen + bodyLength
            val data = ByteBuffer.allocate(rawLength)
            data.putInt(rawLength)
            data.putShort(rawHeaderLen.toShort())
            data.putShort(1)
            data.putInt(operation)
            data.putInt(1)
            msgBytes?.let {
                data.put(msgBytes)
            }
            data.flip()
            return DanmuPack(data)
        }

    }

    init {
        this.packetLength = dataBuffer.getInt(offset + packetOffset)
        this.headerLength = dataBuffer.getShort(offset + headerOffset)
        this.version = dataBuffer.getShort(offset + versionOffset)
        this.operatoion = dataBuffer.getInt(offset + operationOffset)
        this.sequence = dataBuffer.getInt(offset + seqOffset)
        val byteArray = ByteArray(packetLength)
        dataBuffer.get(byteArray, offset, offset + packetLength)
        this.value = ByteBuffer.wrap(byteArray)
        dataBuffer.position(0)
    }


}