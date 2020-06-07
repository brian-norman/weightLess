package com.brian.weightLess

import com.brian.weightLess.data.WeightEntity
import com.robinhood.spark.SparkAdapter

class ChartAdapter(private var data: List<WeightEntity>) : SparkAdapter() {
    override fun getY(index: Int): Float = data[index].weight

    override fun getItem(index: Int): Any = data[index]

    override fun getCount(): Int = data.size

    fun setData(newData: List<WeightEntity>) {
        data = newData
        notifyDataSetChanged()
    }
}
