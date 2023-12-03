package com.example.wattercount.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wattercount.Fragments.HomeFragment
import com.example.wattercount.entities.HistoryItem
import com.example.wattercount.R
import com.example.wattercount.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryAdapter(private val dataList: MutableList<HistoryItem>, private val db: AppDatabase, private val homeFragment: HomeFragment) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val TAG = "debugTag"
    private var isLastItem = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder  {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        val item = dataList[position]

        Log.v(TAG, item.toString())

        holder.timeTextView.text = item.time
        holder.countTextView.text = item.count
        holder.editButton.setOnClickListener {
            val popupMenu = PopupMenu(holder.editButton.context, holder.editButton)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.history_delete -> {
                        val selectedItem = dataList[position]
                        deleteHistoryItem(selectedItem) // Вызов метода для удаления элемента
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        isLastItem = position == dataList.size - 1

        if (isLastItem) {
            holder.separatorView.visibility = View.GONE
        } else {
            holder.separatorView.visibility = View.VISIBLE
        }
    }

    private fun deleteHistoryItem(historyItem: HistoryItem) {
        CoroutineScope(Dispatchers.IO).launch {
            db.historyItemDao().deleteById(historyItem.id)
        }
        dataList.remove(historyItem)
        notifyDataSetChanged()
        homeFragment.updateCurrentCountWater()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.history_drink_time)
        val countTextView: TextView = itemView.findViewById(R.id.history_drink_count)
        val editButton: ImageButton = itemView.findViewById(R.id.history_drink_edit)
        val separatorView: View = itemView.findViewById(R.id.item_separator)
    }
}