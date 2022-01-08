package com.example.proximityworks.network.sockets

import com.example.proximityworks.data.SocketUpdate
import com.example.proximityworks.network.WebServiceProvider.Companion.NORMAL_CLOSURE_STATUS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

@ExperimentalCoroutinesApi
class WebSocketListener: WebSocketListener() {

    val socketEventChannel: Channel<SocketUpdate> = Channel(10)

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        GlobalScope.launch {
            socketEventChannel.send(SocketUpdate(byteString = bytes))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        GlobalScope.launch {
            socketEventChannel.send(SocketUpdate(exception = SocketAbortedException()))
        }
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        socketEventChannel.close()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            socketEventChannel.send(SocketUpdate(exception = t))
        }
    }
}