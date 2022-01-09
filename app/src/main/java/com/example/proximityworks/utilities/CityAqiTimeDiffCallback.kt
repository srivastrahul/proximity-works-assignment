package com.example.proximityworks.utilities

import androidx.recyclerview.widget.DiffUtil
import com.example.proximityworks.data.CityAqiTime

class CityAqiTimeDiffCallback(
    private val oldList: List<CityAqiTime>,
    private val newList: List<CityAqiTime>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].city?.equals(newList[newItemPosition].city) == true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.aqi == newItem.aqi && oldItem.timestamp == newItem.timestamp
    }
}