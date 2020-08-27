package io.github.pdkst.bilibili.danmu

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class DanmuPack(
    val dataBuffer: ByteBuffer,
    private val offset: Int = 0
) : Iterable<DanmuPack> {
    constructor(byteArray: ByteArray) : this(ByteBuffer.wrap(byteArray))

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

        val byteArray = ByteArray(packetLength - headerLength)
        val duplicate = dataBuffer.duplicate()
        duplicate.position(offset + headerLength)
        duplicate.get(byteArray)
        this.value = ByteBuffer.wrap(byteArray)
    }

    fun bodyAsJson(): String {
        return String(value.array())
    }

    fun hasNext(): Boolean {
        return this.offset + this.packetLength < dataBuffer.limit()
    }

    fun next(): DanmuPack {
        dataBuffer.slice()
        return DanmuPack(dataBuffer, this.offset + this.packetLength)
    }

    override fun iterator(): Iterator<DanmuPack> {
        return iterator {
            var danmuPack = this@DanmuPack
            yield(danmuPack)
            while (danmuPack.hasNext()) {
                danmuPack = danmuPack.next()
                yield(danmuPack)
            }
        }
    }

}