package com.example.wattercount

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.wattercount.entities.HistoryItem

@Dao
interface HistoryItemDao {
    @Insert
    suspend fun insert(historyItem: HistoryItem)

    @Query("SELECT * FROM history_items ORDER BY id DESC")
    suspend fun getAll(): List<HistoryItem>

    @Delete
    suspend fun deleteHistoryItem(historyItem: HistoryItem)
}
