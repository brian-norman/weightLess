package com.example.weighttracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weighttracker.data.WeightEntity

class WeightDialogSharedViewModel : ViewModel() {

    private val _newWeightEntity = MutableLiveData<WeightEntity>()
    val newWeightEntity: LiveData<WeightEntity> = _newWeightEntity

    private val _editWeightEntity = MutableLiveData<WeightEntity>()
    val editWeightEntity: LiveData<WeightEntity> = _editWeightEntity

    fun saveNewWeightEntity(weightEntity: WeightEntity) = _newWeightEntity.postValue(weightEntity)

    fun clearNewWeightEntity() = _newWeightEntity.postValue(null)

    fun saveEditWeightEntity(weightEntity: WeightEntity) = _editWeightEntity.postValue(weightEntity)

    fun clearEditWeightEntity() = _editWeightEntity.postValue(null)

}
