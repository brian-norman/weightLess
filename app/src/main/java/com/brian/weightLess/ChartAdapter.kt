package com.brian.weightLess

import com.robinhood.spark.SparkAdapter

class ChartAdapter(private var data: List<Float>) : SparkAdapter() {
    override fun getY(index: Int): Float = data[index]

    override fun getItem(index: Int): Any = data[index]

    override fun getCount(): Int = data.size

    fun setData(newData: List<Float>) {
        data = newData
        notifyDataSetChanged()
    }
}
