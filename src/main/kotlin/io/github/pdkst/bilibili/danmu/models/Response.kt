package io.github.pdkst.bilibili.danmu.models

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ServerListItem(

	@field:SerializedName("port")
	val port: Int? = null,

	@field:SerializedName("host")
	val host: String? = null
)

data class HostServerListItem(

	@field:SerializedName("port")
	val port: Int? = null,

	@field:SerializedName("ws_port")
	val wsPort: Int? = null,

	@field:SerializedName("host")
	val host: String? = null,

	@field:SerializedName("wss_port")
	val wssPort: Int? = null
)

data class Data(

	@field:SerializedName("host_server_list")
	val hostServerList: List<HostServerListItem?>? = null,

	@field:SerializedName("port")
	val port: Int? = null,

	@field:SerializedName("max_delay")
	val maxDelay: Int? = null,

	@field:SerializedName("host")
	val host: String? = null,

	@field:SerializedName("refresh_rate")
	val refreshRate: Int? = null,

	@field:SerializedName("refresh_row_factor")
	val refreshRowFactor: Double? = null,

	@field:SerializedName("server_list")
	val serverList: List<ServerListItem?>? = null,

	@field:SerializedName("token")
	val token: String? = null
)
