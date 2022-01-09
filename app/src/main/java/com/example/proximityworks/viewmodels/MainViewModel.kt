package com.example.proximityworks.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proximityworks.repositories.MainRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val cityAqiHashMap = HashMap<String, ArrayList<Double>>()
    private val _cityHashMapMLD = MutableLiveData<HashMap<String, ArrayList<Double>>>()


    fun subscribeToSocketEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mainRepository.startSocket().consumeEach {
                    if (it.exception == null) {

                        //logic to populate hashmap
                        val citiesJSONArray = JSONArray(it.text)
                        for (i in 0 until citiesJSONArray.length()) {
                            val cityAqiObject = citiesJSONArray[i] as JSONObject
                            val city = cityAqiObject.optString("city")
                            val cityAqi = cityAqiObject.optDouble("aqi")

                            if (cityAqiHashMap.containsKey(city)) {
                                val cityAqiList = cityAqiHashMap[city]
                                cityAqiList?.add(cityAqi)
                                cityAqiHashMap[city] = cityAqiList!!
                            } else cityAqiHashMap[city] = arrayListOf(cityAqi)

                        }
                        _cityHashMapMLD.postValue(cityAqiHashMap)

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

    fun getCityAqiMap(): LiveData<HashMap<String, ArrayList<Double>>> = _cityHashMapMLD

    override fun onCleared() {
        mainRepository.closeSocket()
        super.onCleared()
    }

}