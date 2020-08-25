@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package io.github.pdkst.bilibili.danmu

import com.google.gson.GsonBuilder
import io.github.pdkst.bilibili.danmu.models.Response
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI
import java.time.Duration

fun main() {
    val roomId = 21403601
    val gson = GsonBuilder().disableHtmlEscaping().create()
    val httpClient = OkHttpClient.Builder()
        .connectTimeout(Duration.ofSeconds(30))
        .writeTimeout(Duration.ofSeconds(30))
        .readTimeout(Duration.ofSeconds(30))
        .build()
    val httpUrl = "https://api.live.bilibili.com/room/v1/Danmu/getConf"
        .toHttpUrl()
        .newBuilder()
        .addQueryParameter("room_id", roomId.toString())
        .addQueryParameter("platform", "pc")
        .addQueryParameter("player", "web")
        .build()
    val roomConf = Request.Builder()
        .url(httpUrl)
        .get()
        .build()
    val execute = httpClient.newCall(roomConf).execute()
    execute.body?.let {
        val json = gson.fromJson(it.string(), Response::class.java)
        json?.data?.let {
            val serverListItem = it.hostServerList?.get(0)
            WebclientConfig(
                token = it.token!!,
                domain = serverListItem!!.host!!,
                port = serverListItem.port!!,
                wsPort = serverListItem.wsPort!!,
                wssPort = serverListItem.wssPort!!
            )
        }
    }?.let {
        val uri = URI.create("wss://${it.domain}:${it.wssPort}/sub")
        val client = WebsocketClient(
            uri,
            roomId,
            it.token,
            0
        )
//        client.addHeader("host", it.domain)
//        client.addHeader("origin", "https://live.bilibili.com")
        client.connect()
        while (true) {
            val readLine = readLine()
            println("readline = $readLine")
        }
    }


}