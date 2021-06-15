package com.brian.weightLess

import androidx.lifecycle.ViewModel
import com.brian.weightLess.data.WeightEntity

class WeightDialogSharedViewModel : ViewModel() {

    private val _newWeightEntity = LiveEvent<WeightEntity?>()
    val newWeightEntity: LiveEvent<WeightEntity?> = _newWeightEntity

    private val _editWeightEntity = LiveEvent<WeightEntity?>()
    val editWeightEntity: LiveEvent<WeightEntity?> = _editWeightEntity

    fun saveNewWeightEntity(weightEntity: WeightEntity) = _newWeightEntity.postValue(weightEntity)

    fun saveEditWeightEntity(weightEntity: WeightEntity) = _editWeightEntity.postValue(weightEntity)
}
