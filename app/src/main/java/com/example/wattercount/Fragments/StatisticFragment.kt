package com.example.wattercount.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wattercount.Adapter.StatisticAdapter
import com.example.wattercount.R
import com.example.wattercount.Statistics.DataStatsHolder
import com.example.wattercount.Statistics.Utils
import com.example.wattercount.databinding.FragmentStatisticBinding
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.entities.StatisticItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticFragment : Fragment() {
    private lateinit var binding: FragmentStatisticBinding
    private var dataStatsList: MutableList<StatisticItem> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StatisticAdapter
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater)
        return binding.root

        CoroutineScope(Dispatchers.Main).launch {
//            database.statsItemDao().deleteAll()
            recoverStatsList()
            DataStatsHolder.dataStatsList = dataStatsList
//            if (isWeekFinished(dataStatsList)) {
//                clearDatabase(dataStatsList)
//            }
            if (dataStatsList.isNotEmpty()) {
                setBackgroundForDayOfWeek()
                binding.lastDayResult.text = "${Utils.lastDrunkWaterResult(dataStatsList)} ml"
                binding.weeklyResult.text = "${Utils.calculateWeeklyResult(dataStatsList)} ml / день"
                binding.averageFinishResult.text = "${Utils.calculateAverageFinish(dataStatsList)}"
            }
            setStatsRecycler()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getInstance(requireContext())
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatisticFragment()
    }

    private fun setStatsRecycler() {
        recyclerView = binding.recycleStats
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = StatisticAdapter(dataStatsList)
        recyclerView.adapter = adapter

        if (dataStatsList.isEmpty()) {
            binding.statsHistoryTitleBlank.visibility = View.VISIBLE
        } else {
            binding.statsHistoryTitleBlank.visibility = View.GONE
        }
    }

    private fun isWeekFinished(dataStatsList: MutableList<StatisticItem>): Boolean {
        if (dataStatsList.first().dayOfWeek == 1 || dataStatsList.last().dayOfWeek == 1) {
            return true
        }
        return false
    }

    private suspend fun clearDatabase(dataStatsList: MutableList<StatisticItem>) {
        withContext(Dispatchers.IO) {
            val tempDayValue = dataStatsList.first()
            database.statsItemDao().deleteAll()
            database.statsItemDao().insert(tempDayValue)
            dataStatsList.clear()
            dataStatsList.add(tempDayValue)
            DataStatsHolder.dataStatsList = dataStatsList
        }
    }

//    CoroutineScope(Dispatchers.Main).launch {
//        withContext(Dispatchers.IO) {
//            // Код, выполняющийся в фоновом потоке, например, операции с базой данных
//        }
//        // Код, выполняющийся в главном потоке, например, обновление пользовательского интерфейса
//    }

    // Восстановление статистики из базы данных
    private suspend fun recoverStatsList() {
        withContext(Dispatchers.IO) {
            val statisticItems = database.statsItemDao().getAll()
            dataStatsList.addAll(statisticItems)
        }
    }

    private fun setBackgroundForDayOfWeek() {
        val dayArray: IntArray = dataStatsList.map { it.dayOfWeek }.toIntArray()
        for (dayIndex in dayArray.indices) {
            val day = dayArray[dayIndex]
            if (dataStatsList[dayIndex].isFinishDay) {
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
                        val imageViewId = resources.getIdentifier(currentDay, "id", requireContext().packageName)
                        binding.root.findViewById<ImageView>(imageViewId)?.setBackgroundResource(R.drawable.day_not_finish)
                    }
                }
            } else {
                continue
            }
        }
    }
}