package com.example.wattercount.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wattercount.entities.StatisticItem

@Dao
interface StatisticItemDao {
    @Insert
    suspend fun insert(statisticItem: StatisticItem)

    @Query("SELECT * FROM statistic_items ORDER BY id DESC")
    suspend fun getAll(): List<StatisticItem>

    @Query("DELETE FROM statistic_items")
    suspend fun deleteAll()

    @Query("DELETE FROM statistic_items WHERE id = :itemId")
    suspend fun deleteById(itemId: Int)

}