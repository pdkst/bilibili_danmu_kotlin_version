package io.github.pdkst.bilibili.danmu

import com.google.gson.GsonBuilder
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.nio.ByteBuffer
import java.util.*
import kotlin.concurrent.timerTask

class WebsocketClient(
    uri: URI,
    private val roomId: Int,
    private val token: String,
    private val uid: Int = 0
) : WebSocketClient(uri) {
    var timer: Timer? = null

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("websocket opened ")
        sendAuthPack()
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("websocket onClose code=$code, reason=$reason, remote=$remote")
    }

    override fun onError(ex: Exception?) {
        println("websocket onError ")
        println(ex)
    }

    override fun onMessage(message: String?) {
        println("message = $message")
    }

    override fun onMessage(bytes: ByteBuffer?) {
        println("message buffer = $bytes")
        val danmuPack = bytes?.let {
            DanmuPack(it)
        }?.let {
            messageHandler(it)
        }
    }

    private fun messageHandler(pack: DanmuPack) {
        when (pack.operatoion) {
            Operation.AUTH_REPLY -> {
                timer = timer ?: Timer()
                timer?.let {
                    it.schedule(
                        timerTask {
                            heartBeat()
                        },
                        100L,
                        30 * 1000
                    )
                }
            }
        }
    }

    fun sendAuthPack() {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val map = mapOf<String, Any>(
            "uid" to 2440314,
            "roomid" to roomId,
            "token" to token
        )
        val json = gson.toJson(map)
        println("auth = $json")
        val pack = DanmuPack.packString(json, Operation.AUTH)
        println("pack lenth = ${pack.packetLength}")
        println("pack lenth = ${pack.headerLength}")
        println("pack operatoion = ${pack.operatoion}")
        println("pack rawLength = ${pack.dataBuffer.limit()}")
        send(pack.dataBuffer)
    }

    fun heartBeat() {
        println("heartBeat...")
        val pack = DanmuPack.packString(null, Operation.HEARTBEAT)
        send(pack.dataBuffer)
    }

    /**
     * 未验证格式？
     */
    fun sendMessage(msg: String?) {
        msg?.let {
            DanmuPack.packString(msg, Operation.SEND_MSG)
        }?.let {
            send(it.dataBuffer)
        }
    }

}

