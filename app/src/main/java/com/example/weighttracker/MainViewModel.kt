package com.example.weighttracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _chartData: MutableLiveData<List<WeightEntry>> = MutableLiveData()
    val chartData: LiveData<List<WeightEntry>> = _chartData

    init {
        _chartData.postValue(
            listOf(
                WeightEntry("Days Ago", 100f),
                WeightEntry("Days Ago", 101f),
                WeightEntry("Days Ago", 103f),
                WeightEntry("Days Ago", 104f),
                WeightEntry("Days Ago", 99f)
            )
        )
    }

    fun addNewWeight(weightEntry: WeightEntry) {
        val mutable = _chartData.value?.toMutableList() ?: mutableListOf()
        mutable.add(weightEntry)
        _chartData.value = mutable
    }

}
