package io.github.pdkst.bilibili.danmu

object Operation {
    const val HANDSHAKE = 0
    const val HANDSHAKE_REPLY = 1
    const val HEARTBEAT = 2
    const val HEARTBEAT_REPLY = 3
    const val SEND_MSG = 4
    const val SEND_MSG_REPLY = 5
    const val DISCONNECT_REPLY = 6
    const val AUTH = 7
    const val AUTH_REPLY = 8
    const val RAW = 9
    const val PROTO_READY = 10
    const val PROTO_FINISH = 11
    const val CHANGE_ROOM = 12
    const val CHANGE_ROOM_REPLY = 13
    const val REGISTER = 14
    const val REGISTER_REPLY = 15
    const val UNREGISTER = 16
    const val UNREGISTER_REPLY = 17
    //  B站业务自定义OP
    //  MinBusinessOp = 1000
    //  MaxBusinessOp = 10000
}