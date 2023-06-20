package com.example.wattercount.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wattercount.HistoryItem
import com.example.wattercount.R

class HistoryAdapter(private val dataList: List<HistoryItem>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder  {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        val item = dataList[position]

        holder.timeTextView.text = item.time
        holder.countTextView.text = item.count
        holder.editButton.setOnClickListener {
            val popupMenu = PopupMenu(holder.editButton.context, holder.editButton)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.change_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.history_change -> {
                        // Действие при выборе элемента "Изменить"
                        true
                    }
                    R.id.history_delete -> {
                        // Действие при выборе элемента "Удалить"
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.history_drink_time)
        val countTextView: TextView = itemView.findViewById(R.id.history_drink_count)
        val editButton: ImageButton = itemView.findViewById(R.id.history_drink_edit)
    }
}