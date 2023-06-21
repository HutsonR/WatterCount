package com.example.wattercount.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wattercount.entities.HistoryItem
import com.example.wattercount.entities.StatisticItem

@Database(entities = [HistoryItem::class, StatisticItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyItemDao(): HistoryItemDao
    abstract fun statsItemDao(): StatisticItemDao

    companion object {
        private const val DATABASE_NAME = "water_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}
