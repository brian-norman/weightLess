package com.brian.weightLess.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "weights")
data class WeightEntity (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var date: Long,
    var pounds: Float
)

fun WeightEntity.getDate(): String {
    return SimpleDateFormat("MMM d, yyyy", Locale.US).format(this.date * 1000L)
}
