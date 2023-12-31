package com.example.wattercount.Statistics

import android.content.Context
import android.util.Log
import com.example.wattercount.SharedPreferencesHelper
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.entities.StatisticItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Utils {
    private val TAG = "debugTag"

    fun lastDrunkWaterResult(dataList: List<StatisticItem>): String {
        if (dataList.isNotEmpty()) {
            val lastValue = dataList.first().dayValue.toString()
            return lastValue
        } else {
            return "0"
        }
    }

    fun calculateWeeklyResult(dataList: List<StatisticItem>): String {
        if (dataList.isNotEmpty()) {
            val sumAllDayValue = dataList.sumOf { it.dayValue.toInt() }
            val calcWeek = (sumAllDayValue / 7).toString()
            return calcWeek
        } else {
            return "0"
        }
    }

    fun calculateAverageFinish(dataList: List<StatisticItem>): String {
        if (dataList.isNotEmpty()) {
            val finishDayArray = dataList.map { it.isFinishDay }.toTypedArray()
            val convertedArray = finishDayArray.map { if (it) 1 else 0 }.toIntArray()
            val allFinishSum = convertedArray.sum().toFloat()
            val resFinishDay = ((allFinishSum / 7) * 100).toInt().toString()
            return resFinishDay
        } else {
            return "0"
        }
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
        return currentDate != oldDate
    }

//    проверка вывполнил ли норму выпитой воды для finishDay
    fun isFinish(currentWaterValue: String, finishWaterValue: String): Boolean {
        return currentWaterValue.toInt() >= finishWaterValue.toInt()
    }

    fun addStatsData(dataList: MutableList<StatisticItem>, database: AppDatabase, date: String, dayValue: Int, finishDay: Boolean, dayOfWeek: Int) {
        val statsItem = StatisticItem(date = date, dayValue = dayValue.toString(), isFinishDay = finishDay, dayOfWeek = dayOfWeek)
//        val statsItem2 = StatisticItem(date = "26.06", dayValue = "1300", isFinishDay = false, dayOfWeek = 1)
        // Сохранение элемента в базе данных
        GlobalScope.launch {
            database.statsItemDao().insert(statsItem)
//            database.statsItemDao().insert(statsItem2)
        }
        // Передаем значение в dataStatsHolder
        dataList.add(statsItem)
//        dataList.add(statsItem2)
    }
}