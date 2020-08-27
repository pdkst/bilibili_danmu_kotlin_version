package io.github.pdkst.bilibili.danmu

object WebsocketBodyProtocol {
    // 普通（默认）
    const val VERSION_NORMAL: Short = 0

    // 用于心跳包
    const val VERSION_INT: Short = 1

    // 压缩弹幕，可能包含多条（需要解压 zlib/pako）
    const val VERSION_DEFLATE: Short = 2
}