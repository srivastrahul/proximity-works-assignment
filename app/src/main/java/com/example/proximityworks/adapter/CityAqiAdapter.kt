package com.example.proximityworks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proximityworks.data.CityAqiTime
import com.example.proximityworks.databinding.ItemCityAqiBinding
import com.example.proximityworks.utilities.CityAqiTimeDiffCallback

class CityAqiAdapter(private val onClickLambda: (String?) -> Unit): RecyclerView.Adapter<CityAqiAdapter.CityAqiViewHolder>() {
    var dataList = ArrayList<CityAqiTime>()

    fun setData(cityAqiList: ArrayList<CityAqiTime>) {
        val cityAqiTimeDiffCallback = CityAqiTimeDiffCallback(dataList, cityAqiList)
        val diffResult = DiffUtil.calculateDiff(cityAqiTimeDiffCallback)

        dataList.clear()
        dataList.addAll(cityAqiList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CityAqiViewHolder(private val binding: ItemCityAqiBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind (position: Int) {
            binding.root.setOnClickListener {
                onClickLambda(dataList[position].city)
            }
            binding.setVariable(BR.cityAqiItem, dataList[position])
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAqiViewHolder {
        return CityAqiViewHolder(
            ItemCityAqiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityAqiViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}