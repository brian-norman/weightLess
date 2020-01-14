package com.example.weighttracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _chartData: MutableLiveData<List<Float>> = MutableLiveData()
    val chartData: LiveData<List<Float>> = _chartData

    init {
        _chartData.postValue(listOf(1f, 2f, 3f, 4f, 1f))
    }

    fun addNewWeight(weight: Float) {
        val mutable = _chartData.value?.toMutableList() ?: mutableListOf()
        mutable.add(weight)
        _chartData.value = mutable
    }

}
