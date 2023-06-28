package com.example.wattercount.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wattercount.Adapter.HistoryAdapter
import com.example.wattercount.DialogListener
import com.example.wattercount.FinalWaterListener
import com.example.wattercount.R
import com.example.wattercount.SharedPreferencesHelper
import com.example.wattercount.Statistics.DataStatsHolder
import com.example.wattercount.Statistics.Utils
import com.example.wattercount.databinding.FragmentHomeBinding
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.dialogs.ConfirmFinalWaterCountFragment
import com.example.wattercount.dialogs.VariableDialogFragment
import com.example.wattercount.entities.HistoryItem
import com.example.wattercount.state.hints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class HomeFragment : Fragment(), DialogListener, FinalWaterListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var currentDate: String
    private var currentDrinkCount = "0"
    private var finalCountWater = "0"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private var dataList: MutableList<HistoryItem> = mutableListOf()
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getInstance(requireContext())

        //        SharedPreferencesHelper.setOldDateValue(this, "24.06.23")

        onceSetFinalWaterCount()
        hintUpdate()
        GlobalScope.launch {
            // Добавление при запуске всех элементов в историю
            val historyItems = database.historyItemDao().getAll()
            dataList.addAll(historyItems)
            adapter.notifyDataSetChanged()
            // Обновляем текущее значение воды
            updateCurrentCountWater()
            // Проверяем изменен ли день
            addDataToStats()
            // Обновляем текущее значение воды после проверки смены дня
            updateCurrentCountWater()
        }
        setCurrentDate()
        setWaterCounts()
        setAddButton()
        setQuickAddButton()
        setHistoryRecycler()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private fun hintUpdate() {
        val randomHint = hints.random()
        binding.randomHint.text = randomHint.text
    }

    private fun onceSetFinalWaterCount() {
        val setFinalCountWater = SharedPreferencesHelper.getFinalWaterCount(requireContext())
        if (setFinalCountWater == 0) {
            ConfirmFinalWaterCountFragment(R.layout.final_count_fragment).show(childFragmentManager, "final water")
        }
    }

    private fun setWaterCounts() {
        currentDrinkCount = dataList.sumOf { it.count.toInt() }.toString()
        finalCountWater = SharedPreferencesHelper.getFinalWaterCount(requireContext()).toString()
        binding.finalCountWater.text = finalCountWater
        binding.percentCountWater.text = "${calcPercent()}%"
    }

    private fun setCurrentDate() {
        // Установка текущей даты
        currentDate = getCurrentDate()
        SharedPreferencesHelper.setCurrentDateValue(requireContext(), currentDate)
        binding.currentDate.text = currentDate
    }

    private fun setAddButton() {
        binding.customPopupAdd.setOnClickListener {
            VariableDialogFragment(R.layout.add_fragment).show(childFragmentManager, "add fragment")
        }
    }

    override fun onConfirmAddDialogResult(data: String) {
        addHistoryItem(data)
        currentDrinkCount = (binding.currentCountWater.text.toString().toInt() + data.toInt()).toString()
        binding.currentCountWater.text = currentDrinkCount
        binding.percentCountWater.text = "${calcPercent()}%"
    }

    override fun onConfirmFinalResult(data: String) {
        var finalCountWater = binding.finalCountWater.text
        finalCountWater = data
        val finalCountWaterInt = finalCountWater.toInt()
        SharedPreferencesHelper.setFinalWaterCount(requireContext(), finalCountWaterInt)
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR) % 100
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%02d.%02d.%02d", day, month, year)
    }

    private fun addHistoryItem(count: String) {
        val currentTime = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("HH:mm")
        val formattedTime = timeFormat.format(currentTime)

        val historyItem = HistoryItem(time = formattedTime, count = "$count")
        // Сохранение элемента в базе данных
        GlobalScope.launch {
            database.historyItemDao().insert(historyItem)
        }

        dataList.add(0, historyItem)
        adapter.notifyItemInserted(0)
    }

    private fun setQuickAddButton() {
        // Новый обработчик нажатий на маленький размер (150мл)
        binding.smallMlStandart.setOnClickListener {
            handleStandardButtonClick(150)
            addHistoryItem(150.toString())
        }
        // Новый обработчик нажатий на стандартный размер (250мл)
        binding.mediumMlStandart.setOnClickListener {
            handleStandardButtonClick(250)
            addHistoryItem(250.toString())
        }
        // Новый обработчик нажатий на увеличенный размер (350мл)
        binding.standartMlStandart.setOnClickListener {
            handleStandardButtonClick(350)
            addHistoryItem(350.toString())
        }
    }

    // Обработка нажатия на быструю кнопку добавления
    private fun handleStandardButtonClick(size: Int) {
        val currentCount = binding.currentCountWater.text.toString().toInt()
        val newCount = currentCount + size
        binding.currentCountWater.text = newCount.toString()
        binding.percentCountWater.text = "${calcPercent()}%"
    }

    private fun calcPercent(): Int {
        val currentCountWaterInt = binding.currentCountWater.text.toString().toInt()
        val finalCountWaterInt = binding.finalCountWater.text.toString().toInt()

        return if (finalCountWaterInt != 0 && currentCountWaterInt > 0) {
            (currentCountWaterInt * 100) / finalCountWaterInt
        } else {
            0
        }
    }

    // Обновление текущего значения выпитой воды из истории
    fun updateCurrentCountWater() {
        currentDrinkCount = dataList.sumOf { it.count.toInt() }.toString()
        binding.currentCountWater.text = currentDrinkCount
        binding.percentCountWater.text = "${calcPercent()}%"
    }

    private fun addDataToStats() {
        if (Utils.isDifferentDate(requireContext())) {
            val finishDay = Utils.isFinish(currentDrinkCount, finalCountWater)
            val dataStatsList = DataStatsHolder.dataStatsList
            val date = SharedPreferencesHelper.getOldDateValue(requireContext())!!
            val oldDate = currentDate
            SharedPreferencesHelper.setOldDateValue(requireContext(), oldDate)
            val formattedDate = date.substring(0, date.length - 3)

            Utils.addStatsData(dataStatsList, database, formattedDate, currentDrinkCount.toInt(), finishDay, calcDayOfWeek())

            // Очистка текущих значений
            runBlocking {
                withContext(Dispatchers.IO) {
                    database.historyItemDao().deleteAll()
                }

                // Обновление списка и адаптера
                dataList.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun calcDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        // Преобразование значения дня недели в формат от 1 (понедельник) до 7 (воскресенье)
        val adjustedDayOfWeek = if (dayOfWeek == Calendar.SUNDAY) 7 else dayOfWeek - 1

        var calcDay = adjustedDayOfWeek

        if (adjustedDayOfWeek == 1) {
            calcDay = 7
        } else {
            calcDay -= 1
        }

        return calcDay
    }

    private fun setHistoryRecycler() {
        recyclerView = binding.recycleHistory
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HistoryAdapter(dataList, database, this)
        recyclerView.adapter = adapter
    }
}
