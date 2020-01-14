package com.example.weighttracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeightDialogSharedViewModel : ViewModel() {

    private val _newWeightEntry = MutableLiveData<WeightEntry>()
    val newWeightEntry: LiveData<WeightEntry> = _newWeightEntry

    fun saveNewWeightEntry(weightEntry: WeightEntry) = _newWeightEntry.postValue(weightEntry)

    fun clearNewWeightEntry() = _newWeightEntry.postValue(null)

}
