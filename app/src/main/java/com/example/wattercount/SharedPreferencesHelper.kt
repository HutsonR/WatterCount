package com.example.wattercount

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREFS_NAME = "app_prefs"
    private const val PREF_CURRENT_WATER_COUNT = "current_water_count"
    private const val PREF_FINAL_WATER_COUNT = "final_water_count"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


//    fun getCurrentWaterCount(context: Context): Int {
//        val prefs = getSharedPreferences(context)
//        return prefs.getInt(PREF_CURRENT_WATER_COUNT, 0)
//    }
//
//    fun setCurrentWaterCount(context: Context, count: Int) {
//        val prefs = getSharedPreferences(context)
//        prefs.edit().putInt(PREF_CURRENT_WATER_COUNT, count).apply()
//    }


    fun getFinalWaterCount(context: Context): Int {
        val prefs = getSharedPreferences(context)
        return prefs.getInt(PREF_FINAL_WATER_COUNT, 0)
    }

    fun setFinalWaterCount(context: Context, count: Int) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putInt(PREF_FINAL_WATER_COUNT, count).apply()
    }
}
