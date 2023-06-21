package com.example.wattercount.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.wattercount.DataStatsHolder
import com.example.wattercount.R
import com.example.wattercount.Utils
import com.example.wattercount.databinding.ActivityStatisticsBinding
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.entities.StatisticItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StatisticsActivity : AppCompatActivity() {
    private val TAG = "debugTag"

    private lateinit var binding: ActivityStatisticsBinding
    private var dataStatsList: MutableList<StatisticItem> = mutableListOf()
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
            Log.v(TAG, "before recover $dataStatsList")
            recoverStatsList()
            Log.v(TAG, "after recover $dataStatsList")
            DataStatsHolder.dataStatsList = dataStatsList
            Log.v(TAG, "after holder $dataStatsList")
//            Utils.addStatsData(dataList, database, 1800, true, 3)
//            Utils.addStatsData(dataList, database, 1300, false, 1)
//            Utils.addStatsData(dataList, database, 2000, true, 2)

            if (dataStatsList != null) {
                setBackgroundForDayOfWeek()
                binding.lastDayResult.text = "${Utils.lastDrunkWaterResult(dataStatsList)} ml"
                binding.weeklyResult.text = "${Utils.calculateWeeklyResult(dataStatsList)} ml / день"
                binding.averageFinishResult.text = "${Utils.calculateAverageFinish(dataStatsList)}"
            }
        }
    }

    private suspend fun recoverStatsList() {
        withContext(Dispatchers.IO) {
            val statisticItems = database.statsItemDao().getAll()
            dataStatsList.addAll(statisticItems)
        }
    }


    private fun setBackgroundForDayOfWeek() {
        val dayArray = dataStatsList.map { it.dayOfWeek }.toIntArray()
        for (day in dayArray) {
            when (day) {
                1 -> {
                    binding.imageView1.setBackgroundResource(R.drawable.day_finish)
                }
                2 -> {
                    binding.imageView2.setBackgroundResource(R.drawable.day_finish)
                }
                3 -> {
                    binding.imageView3.setBackgroundResource(R.drawable.day_finish)
                }
                4 -> {
                    binding.imageView4.setBackgroundResource(R.drawable.day_finish)
                }
                5 -> {
                    binding.imageView5.setBackgroundResource(R.drawable.day_finish)
                }
                6 -> {
                    binding.imageView6.setBackgroundResource(R.drawable.day_finish)
                }
                7 -> {
                    binding.imageView7.setBackgroundResource(R.drawable.day_finish)
                }
                else -> {
                    val currentDay = "imageView$day"
                    val imageViewId = resources.getIdentifier(currentDay, "id", packageName)
                    binding.root.findViewById<ImageView>(imageViewId)?.setBackgroundResource(R.drawable.day_not_finish)
                }
            }
        }
    }
}
