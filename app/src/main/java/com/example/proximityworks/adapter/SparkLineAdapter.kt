package com.example.proximityworks.adapter

import com.robinhood.spark.SparkAdapter

class SparkLineAdapter(private var yData: FloatArray): SparkAdapter() {


    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }
}