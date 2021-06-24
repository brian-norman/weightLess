package com.brian.weightLess

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.brian.weightLess.data.WeightDao
import com.brian.weightLess.data.WeightEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weightDao: WeightDao) : ViewModel() {

    // TODO: Use PagedList from Paging Library
    val weightEntities: LiveData<List<WeightEntity>> = weightDao.getAll().asLiveData()

    fun insertWeight(weightEntity: WeightEntity) {
        viewModelScope.launch { weightDao.insert(weightEntity) }
    }

    fun updateWeight(weightEntity: WeightEntity) {
        viewModelScope.launch { weightDao.update(weightEntity) }
    }

    fun deleteWeight(weightEntity: WeightEntity) {
        viewModelScope.launch { weightDao.delete(weightEntity) }
    }

    fun deleteWeight(position: Int): WeightEntity {
        val weight = weightEntities.value!![position]
        viewModelScope.launch { weightDao.delete(weight) }
        return weight
    }
}
