package com.brian.weightLess

import androidx.lifecycle.*
import com.brian.weightLess.data.WeightDao
import com.brian.weightLess.data.WeightEntity
import kotlinx.coroutines.launch

class MainViewModel(private val weightDao: WeightDao) : ViewModelProvider.Factory, ViewModel() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(weightDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

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
