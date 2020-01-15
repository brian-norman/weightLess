package com.example.weighttracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weighttracker.data.WeightDao
import com.example.weighttracker.data.WeightEntity
import kotlinx.coroutines.launch

class MainViewModel(private val weightDao: WeightDao) : ViewModel() {

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
