package com.example.proximityworks.repositories

import com.example.proximityworks.data.SocketUpdate
import com.example.proximityworks.network.WebServiceProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class MainRepository @Inject constructor(private val webServicesProvider: WebServiceProvider) {

    @ExperimentalCoroutinesApi
    fun startSocket(): Channel<SocketUpdate> =
        webServicesProvider.startSocket()

    @ExperimentalCoroutinesApi
    fun closeSocket() {
        webServicesProvider.stopSocket()
    }
}