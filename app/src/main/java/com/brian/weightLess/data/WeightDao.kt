package com.brian.weightLess.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {

    @Query("SELECT * FROM weights ORDER BY date DESC")
    fun getAll(): Flow<List<WeightEntity>>

    @Insert
    suspend fun insert(weightEntity: WeightEntity)

    @Delete
    suspend fun delete(weightEntity: WeightEntity)

    @Update
    suspend fun update(weightEntity: WeightEntity)

}
