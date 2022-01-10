package com.example.proximityworks.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proximityworks.BaseApplication
import com.example.proximityworks.data.AqiTime
import com.example.proximityworks.data.CityAqiTime
import com.example.proximityworks.repositories.MainRepository
import com.example.proximityworks.utilities.ValidationCheckConstants
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val validationCheckMLD = MutableLiveData<Int>()
    private val cityAqiHashMap = HashMap<String, ArrayList<AqiTime>>()
    private val _cityHashMapMLD = MutableLiveData<HashMap<String, ArrayList<AqiTime>>>()
    var cityAqiTimeList = ArrayList<CityAqiTime>()
    var aqiList = ArrayList<AqiTime>()
    var timestamp: Long = 0L
    private val _lastUpdatedCityAqiList = MutableLiveData<ArrayList<CityAqiTime>>()
    var selectedCityAqiList = ArrayList<AqiTime>()
    var selectedTImeIntervalBasedAqiList =
        ArrayList<AqiTime>()  //time interval of 30s from current timestamp
    var selectedCity: String = ""


    fun subscribeToSocketEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mainRepository.startSocket().consumeEach {
                    if (it.exception == null) {

                        //logic to populate hashmap to store all entries
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
                                } else cityAqiHashMap[city] =
                                    arrayListOf(AqiTime(cityAqi, timestamp))

                            }
                            _cityHashMapMLD.postValue(cityAqiHashMap)

                            //for recycler view adapter
                            cityAqiTimeList = ArrayList()
                            for (item in cityAqiHashMap.entries) {
                                aqiList = item.value
                                val aqiListItem = aqiList[aqiList.size - 1]
                                cityAqiTimeList.add(
                                    CityAqiTime(
                                        item.key,
                                        aqiListItem.aqi,
                                        aqiListItem.timestamp
                                    )
                                )
                            }

                            _lastUpdatedCityAqiList.postValue(cityAqiTimeList)
                        }

                    } else {
                        onSocketError(it.exception)
                        validationCheckMLD.postValue(ValidationCheckConstants.EXCEPTION_NO_RESPONSE)
                    }
                }

            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
                validationCheckMLD.postValue(ValidationCheckConstants.EXCEPTION_NO_RESPONSE)
            }
        }
    }

    private fun onSocketError(ex: Throwable) {
        println("Error occurred : ${ex.message}")
        validationCheckMLD.postValue(ValidationCheckConstants.EXCEPTION_NO_RESPONSE)
    }

    fun getCityAqiMap(): LiveData<HashMap<String, ArrayList<AqiTime>>> = _cityHashMapMLD

    fun getLastUpdatedCityAqiList(): LiveData<ArrayList<CityAqiTime>> = _lastUpdatedCityAqiList

    fun closeSocket() {
        mainRepository.closeSocket()
    }

    override fun onCleared() {
        mainRepository.closeSocket()
        super.onCleared()
    }

    fun getValidationCheck(): LiveData<Int> = validationCheckMLD

    fun createBarData(): BarData {
        val values = ArrayList<BarEntry>()
        //reverse selectedCityAqiList to start from latest entry
        selectedCityAqiList.reverse()
        selectedTImeIntervalBasedAqiList.clear()
        //populating only 6 entries in reverse chronological order for display
        selectedTImeIntervalBasedAqiList.add(selectedCityAqiList[0])
        for (i in 1 until selectedCityAqiList.size) {
            if (selectedTImeIntervalBasedAqiList.size == 6) {
                break
            } else if (selectedTImeIntervalBasedAqiList[selectedTImeIntervalBasedAqiList.size - 1].timestamp!! - selectedCityAqiList[i].timestamp!! >= 30000L) {
                selectedTImeIntervalBasedAqiList.add(selectedCityAqiList[i])
            }
        }

        for (i in 0 until selectedTImeIntervalBasedAqiList.size)
            values.add(
                BarEntry(
                    (i + 1).toFloat(),
                    selectedTImeIntervalBasedAqiList[i].aqi?.toFloat()!!
                )
            )
        val barDataSet = BarDataSet(values, "AQI Index")
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(barDataSet)
        return BarData(dataSets)
    }

    //for spark lines
    fun getSelectedCityAqiArray() =
        FloatArray(selectedTImeIntervalBasedAqiList.size) { i -> selectedTImeIntervalBasedAqiList[i].aqi?.toFloat()!! }

    //for time formatting on x-axis
    fun getSelectedCityTimeStampArray() =
        LongArray(selectedTImeIntervalBasedAqiList.size) { i -> selectedTImeIntervalBasedAqiList[i].timestamp!! }

}