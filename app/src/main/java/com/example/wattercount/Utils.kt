package com.example.wattercount

import android.content.Context
import android.util.Log
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.entities.StatisticItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Utils {
    private val TAG = "debugTag"

    fun lastDrunkWaterResult(dataList: List<StatisticItem>): String {
        val lastValue = dataList.first().dayValue.toString()
        return lastValue
    }

    fun calculateWeeklyResult(dataList: List<StatisticItem>): String {
        val sumAllDataValue = dataList.sumOf { it.dayValue.toInt() }
        val calcWeek = (sumAllDataValue / 7).toString()
        return calcWeek
    }

    fun calculateAverageFinish(): Int {
        // Вычислите среднее значение завершения на основе данных из dataList
        // Верните результат в виде числа с плавающей запятой (Float)
        return TODO("Provide the return value")
    }

    fun isDifferentDate(context: Context): Boolean {
        var currentDate = ""
        var oldDate = ""
        if (SharedPreferencesHelper.getCurrentDateValue(context) != null) {
            currentDate = SharedPreferencesHelper.getCurrentDateValue(context).toString()
        }
        if (SharedPreferencesHelper.getOldDateValue(context) != null) {
            oldDate = SharedPreferencesHelper.getOldDateValue(context).toString()
        }
        Log.v(TAG, "currentDate $currentDate")
        Log.v(TAG, "oldDate $oldDate")
        if (currentDate == oldDate) {
            return false
        } else {
            oldDate = currentDate
            return true
        }
    }

    fun isFinish(currentWaterValue: String, finishWaterValue: String): Boolean {
        return currentWaterValue >= finishWaterValue
    }

    fun addStatsData(dataList: MutableList<StatisticItem>, database: AppDatabase, dayValue: Int, finishDay: Boolean) {
        val statsItem = StatisticItem(dayValue = dayValue.toString(), finishDay = finishDay)
        // Сохранение элемента в базе данных
        GlobalScope.launch {
            database.statsItemDao().insert(statsItem)
        }
        dataList.add(statsItem)
    }
}