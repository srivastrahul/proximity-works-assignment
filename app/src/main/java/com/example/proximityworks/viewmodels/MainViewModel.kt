package com.example.proximityworks.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proximityworks.data.AqiTime
import com.example.proximityworks.data.CityAqiTime
import com.example.proximityworks.repositories.MainRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val cityAqiHashMap = HashMap<String, ArrayList<AqiTime>>()
    private val _cityHashMapMLD = MutableLiveData<HashMap<String, ArrayList<AqiTime>>>()
    var cityAqiTimeList = ArrayList<CityAqiTime>()
    var aqiList = ArrayList<AqiTime>()
    var timestamp: Long = 0L
    private val _lastUpdatedCityAqiList = MutableLiveData<ArrayList<CityAqiTime>>()


    fun subscribeToSocketEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mainRepository.startSocket().consumeEach {
                    if (it.exception == null) {

                        //logic to populate hashmap
                        val citiesJSONArray = JSONArray(it.text)
                        if (citiesJSONArray.length() > 0) {
                            timestamp = Calendar.getInstance().timeInMillis
                            for (i in 0 until citiesJSONArray.length()) {
                                val cityAqiObject = citiesJSONArray[i] as JSONObject
                                val city = cityAqiObject.optString("city")
                                val cityAqi = cityAqiObject.optDouble("aqi")

                                if (cityAqiHashMap.containsKey(city)) {
                                    val cityAqiList = cityAqiHashMap[city]
                                    cityAqiList?.add(AqiTime(cityAqi, timestamp))
                                    cityAqiHashMap[city] = cityAqiList!!
                                } else cityAqiHashMap[city] = arrayListOf(AqiTime(cityAqi, timestamp))

                            }
                            _cityHashMapMLD.postValue(cityAqiHashMap)

                            cityAqiTimeList = ArrayList()
                            for (item in cityAqiHashMap.entries) {
                                aqiList = item.value
                                val aqiListItem = aqiList[aqiList.size-1]
                                cityAqiTimeList.add(CityAqiTime(item.key, aqiListItem.aqi, aqiListItem.timestamp))
                            }

                            _lastUpdatedCityAqiList.postValue(cityAqiTimeList)
                        }

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

    fun getCityAqiMap(): LiveData<HashMap<String, ArrayList<AqiTime>>> = _cityHashMapMLD

    fun getLastUpdatedCityAqiList(): LiveData<ArrayList<CityAqiTime>> = _lastUpdatedCityAqiList

    override fun onCleared() {
        mainRepository.closeSocket()
        super.onCleared()
    }

}