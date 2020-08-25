package io.github.pdkst.bilibili.danmu

import org.junit.Assert.*
import org.junit.Test

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class TestUtilKtTest{
    @Test
    fun testHeartBeat(){
        val pack = DanmuPack.packString(null)
        assert(pack.headerLength.toInt() == 16)
        assert(pack.version.toInt() == 1)
        assert(pack.operatoion == Operation.HEARTBEAT)
    }

    @Test
    fun testPackString1(){
        val pack = DanmuPack.packString("1", Operation.AUTH)
        assert(pack.packetLength > 16)
        assert(pack.headerLength.toInt() == 16)
        assert(pack.operatoion == Operation.AUTH)
        assert(pack.value.get(16).toChar() == '1')
    }

    @Test
    fun testFirstAuth(){

    }
}