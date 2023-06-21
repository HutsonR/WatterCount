package com.example.wattercount.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.wattercount.R
import com.example.wattercount.Utils
import com.example.wattercount.databinding.ActivityStatisticsBinding
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.entities.StatisticItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : AppCompatActivity() {
    private val TAG = "debugTag"

    private lateinit var binding: ActivityStatisticsBinding
    private var dataList: MutableList<StatisticItem> = mutableListOf()
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val closeButton = findViewById<ImageButton>(R.id.close_history_activity)
        closeButton.setOnClickListener {
            finish()
        }

        database = AppDatabase.getInstance(this)

        CoroutineScope(Dispatchers.Main).launch {
            recoverStatsList()

            binding.lastDayResult.text = "${Utils.lastDrunkWaterResult(dataList)} ml"
            binding.weeklyResult.text = "${Utils.calculateWeeklyResult(dataList)} ml / день"
        }
    }

    private suspend fun recoverStatsList() {
        withContext(Dispatchers.IO) {
            val statisticItems = database.statsItemDao().getAll()
            dataList.addAll(statisticItems)
        }
    }
}
