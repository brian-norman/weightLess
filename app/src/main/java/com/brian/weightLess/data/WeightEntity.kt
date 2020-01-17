package com.brian.weightLess.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weights")
data class WeightEntity (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var date: Long,
    var weight: Float
)
