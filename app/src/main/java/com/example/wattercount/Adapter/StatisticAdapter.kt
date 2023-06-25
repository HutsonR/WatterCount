package com.example.wattercount.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wattercount.MainActivity
import com.example.wattercount.entities.HistoryItem
import com.example.wattercount.R
import com.example.wattercount.db.AppDatabase
import com.example.wattercount.entities.StatisticItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StatisticAdapter(private val dataList: MutableList<StatisticItem>) : RecyclerView.Adapter<StatisticAdapter.StatisticViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticAdapter.StatisticViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.statistic_item, parent, false)
        return StatisticAdapter.StatisticViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticAdapter.StatisticViewHolder, position: Int) {
        val item = dataList[position]

        holder.dateTextView.text = item.date
        holder.countTextView.text = item.dayValue
        if (item.isFinishDay) {
            holder.finalStick.setBackgroundResource(R.drawable.statistic_stick_background_active)
        } else {
            holder.finalStick.setBackgroundResource(R.drawable.statistic_stick_background_inactive)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class StatisticViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.stats_date)
        val countTextView: TextView = itemView.findViewById(R.id.stats_drink_count)
        var finalStick: ImageView = itemView.findViewById(R.id.stats_stick)
    }

}