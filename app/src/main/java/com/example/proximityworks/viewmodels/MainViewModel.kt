package com.example.proximityworks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proximityworks.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    @ExperimentalCoroutinesApi
    fun subscribeToSocketEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainRepository.startSocket().consumeEach {
                    if (it.exception == null) {
                        println("Collecting : ${it.text}")
                    } else {
                        onSocketError(it.exception)
                    }
                }
            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
            }
        }
    }

    private fun onSocketError(ex: Throwable) {
        println("Error occurred : ${ex.message}")
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        mainRepository.closeSocket()
        super.onCleared()
    }

}