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
        if (currentDate == oldDate) {
            return false
        } else {
            oldDate = currentDate
            SharedPreferencesHelper.setOldDateValue(context, oldDate)
            return true
        }
    }

//    проверка вывполнил ли норму выпитой воды для finishDay
    fun isFinish(currentWaterValue: String, finishWaterValue: String): Boolean {
        return currentWaterValue.toInt() >= finishWaterValue.toInt()
    }

    fun addStatsData(dataList: MutableList<StatisticItem>, database: AppDatabase, dayValue: Int, finishDay: Boolean, dayOfWeek: Int) {
        val statsItem = StatisticItem(dayValue = dayValue.toString(), isFinishDay = finishDay, dayOfWeek = dayOfWeek - 2)
        // Сохранение элемента в базе данных
        GlobalScope.launch {
            database.statsItemDao().insert(statsItem)
        }
        dataList.add(statsItem)
    }
}