package io.github.pdkst.bilibili.danmu

/**
 * websocket参数配置
 */
data class WebclientConfig(
    val token: String,
    val domain: String = "broadcastlv.chat.bilibili.com",
    val port: Int = 2243,
    val wsPort: Int = 2243,
    val wssPort: Int = 2244,
) {}