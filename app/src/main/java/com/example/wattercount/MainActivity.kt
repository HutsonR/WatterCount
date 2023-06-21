package com.example.wattercount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wattercount.Activity.StatisticsActivity
import com.example.wattercount.Adapter.HistoryAdapter
import com.example.wattercount.databinding.ActivityMainBinding
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.dialogs.ConfirmFinalWaterCountFragment
import com.example.wattercount.dialogs.VariableDialogFragment
import com.example.wattercount.entities.HistoryItem
import com.example.wattercount.entities.StatisticItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity(), DialogListener, FinalWaterListener {
    private val TAG = "debugTag"

    private lateinit var binding: ActivityMainBinding
    private var currentDrinkCount = "0"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private var dataList: MutableList<HistoryItem> = mutableListOf()
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val setFinalCountWater = SharedPreferencesHelper.getFinalWaterCount(this)
        if (setFinalCountWater == 0) {
            ConfirmFinalWaterCountFragment(R.layout.final_count_fragment).show(supportFragmentManager, "final water")
        }

        database = AppDatabase.getInstance(this)
        setStatsListener()

        GlobalScope.launch {
            val historyItems = database.historyItemDao().getAll()
            dataList.addAll(historyItems)
            adapter.notifyDataSetChanged()
            updateCurrentCountWater()
        }

        setHistoryRecycler()

        val finalCountWater = SharedPreferencesHelper.getFinalWaterCount(this)
        binding.finalCountWater.text = finalCountWater.toString()
        binding.percentCountWater.text = "${calcPercent()}%"


        // Установка текущей даты
        val currentDate = getCurrentDate()
        SharedPreferencesHelper.setCurrentDateValue(this, currentDate)
        binding.currentDate.text = currentDate

        binding.customPopupAdd.setOnClickListener {
            VariableDialogFragment(R.layout.add_fragment).show(supportFragmentManager, "add fragment")
        }
        val test: Boolean = Utils.isDifferentDate(this)
        Log.v(TAG, "test $test")
        setupStandartAddButton()
    }


    override fun onFinalResult(data: String) {
        var finalCountWater = binding.finalCountWater.text
        finalCountWater = data
        val finalCountWaterInt = finalCountWater.toInt()
        SharedPreferencesHelper.setFinalWaterCount(this, finalCountWaterInt)

    }


    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR) % 100
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%02d.%02d.%02d", day, month, year)
    }

//    fun onItemClick(article: Article) {
//        val intent = Intent(this, NewsActivity::class.java)
//        intent.putExtra("article", article)
//        startActivity(intent)
//    }

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


    override fun onDialogResult(data: String) {
        addHistoryItem(data)
        currentDrinkCount = (binding.currentCountWater.text.toString().toInt() + data.toInt()).toString()
        binding.currentCountWater.text = currentDrinkCount
        binding.percentCountWater.text = "${calcPercent()}%"
    }


    private fun setupStandartAddButton() {
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


    private fun setStatsListener() {
        binding.statsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }


    fun updateCurrentCountWater() {
        currentDrinkCount = dataList.sumOf { it.count.toInt() }.toString()
        binding.currentCountWater.text = currentDrinkCount
        binding.percentCountWater.text = "${calcPercent()}%"
    }


    private fun setHistoryRecycler() {
        recyclerView = binding.recycleHistory
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HistoryAdapter(dataList, database, this)
        recyclerView.adapter = adapter
    }

}