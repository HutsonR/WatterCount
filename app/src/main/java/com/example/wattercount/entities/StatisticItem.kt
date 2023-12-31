package com.example.wattercount.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistic_items")
data class StatisticItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val dayValue: String,
    val isFinishDay: Boolean = false,
    val dayOfWeek: Int
)