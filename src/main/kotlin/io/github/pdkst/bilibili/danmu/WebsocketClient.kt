package io.github.pdkst.bilibili.danmu

import com.google.gson.GsonBuilder
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.ByteBuffer
import java.util.*
import java.util.zip.InflaterInputStream
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
        ex?.printStackTrace()
    }

    override fun onMessage(message: String?) {
        println("message = $message")
    }

    override fun onMessage(bytes: ByteBuffer?) {
        println("message buffer = $bytes")
        bytes?.let {
            var limit = bytes.limit()
            ByteArray(limit).let {
                bytes.get(it, 0, limit)
                ByteBuffer.wrap(it)
            }
        }?.let {
            DanmuPack(it)
        }?.let {
            messageHandler(it)
        }
    }

    private fun messageHandler(pack: DanmuPack) {
        println("handler = ${pack.operatoion}")
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

            Operation.HEARTBEAT_REPLY -> {
                //int 为在线人数
                println("online = " + pack.dataBuffer.getInt(DanmuPack.packetOffset))
            }

            Operation.SEND_MSG_REPLY -> {
                for (danmuPack in pack.iterator()) {
                    if (danmuPack.version == WebsocketBodyProtocol.VERSION_DEFLATE) {
                        //解压
                        println("deflate pack ...")
                        try {
                            ByteArrayInputStream(danmuPack.value.array())
                                .let { InflaterInputStream(it) }
                                .use { input ->
                                    ByteArrayOutputStream().let {
                                        input.copyTo(it)
                                        it.toByteArray()
                                        DanmuPack(it.toByteArray())
                                    }.let {
                                        println("deflate pack ... ${it.bodyAsJson()}")
                                        messageHandler(it)
                                    }
                                }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        println("msg = ${danmuPack.bodyAsJson()}")
                    }
                }
            }
        }
    }

    fun sendAuthPack() {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val map = mapOf<String, Any>(
            "uid" to uid,
            "roomid" to roomId,
            "protover" to 2,
            "platform" to "web",
            "clientver" to "1.14.3",
            "type" to 2,
            "key" to token
        )
        val json = gson.toJson(map)
        println("send auth = $json")
        val pack = DanmuPack.packString(json, Operation.AUTH)
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

