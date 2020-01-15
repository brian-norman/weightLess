package com.example.weighttracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeightDialogSharedViewModel : ViewModel() {

    private val _newWeightEntry = MutableLiveData<WeightEntry>()
    val newWeightEntry: LiveData<WeightEntry> = _newWeightEntry

    private val _editWeightEntry = MutableLiveData<WeightEntry>()
    val editWeightEntry: LiveData<WeightEntry> = _editWeightEntry

    fun saveNewWeightEntry(weightEntry: WeightEntry) = _newWeightEntry.postValue(weightEntry)

    fun clearNewWeightEntry() = _newWeightEntry.postValue(null)

    fun saveEditWeightEntry(weightEntry: WeightEntry) = _editWeightEntry.postValue(weightEntry)

    fun clearEditWeightEntry() = _editWeightEntry.postValue(null)

}
