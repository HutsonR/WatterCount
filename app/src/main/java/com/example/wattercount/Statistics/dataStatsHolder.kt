package com.example.wattercount.Statistics

import com.example.wattercount.entities.StatisticItem

// Для передачи списка между MainActivity и Utils для StatisticsActivity
object DataStatsHolder {
    var dataStatsList: MutableList<StatisticItem> = mutableListOf()
}