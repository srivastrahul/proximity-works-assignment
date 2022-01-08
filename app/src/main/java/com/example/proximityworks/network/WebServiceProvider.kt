package com.example.proximityworks.network

import com.example.proximityworks.data.SocketUpdate
import com.example.proximityworks.network.sockets.WebSocketListener
import kotlinx.coroutines.channels.Channel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WebServiceProvider @Inject constructor(){
    private var _webSocket: WebSocket? = null

    private val socketOkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(39, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true }
        .build()


    private var _webSocketListener: WebSocketListener? = null

    fun startSocket(): Channel<SocketUpdate> =
        with(WebSocketListener()) {
            startSocket(this)
            this@with.socketEventChannel
        }

    fun startSocket(webSocketListener: WebSocketListener) {
        _webSocketListener = webSocketListener
        _webSocket = socketOkHttpClient.newWebSocket(
            Request.Builder().url("ws://city-ws.herokuapp.com/").build(),
            webSocketListener
        )
        socketOkHttpClient.dispatcher.executorService.shutdown()
    }

    fun stopSocket() {
        try {
            _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            _webSocket = null
            _webSocketListener?.socketEventChannel?.close()
            _webSocketListener = null
        } catch (ex: Exception) {
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}