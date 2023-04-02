package com.example.cars10zes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    var historyItems: List<HistoryItem>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val textUser = holder.itemView.findViewById<TextView>(R.id.text_user)
        val textProject = holder.itemView.findViewById<TextView>(R.id.text_project)
        val textDate = holder.itemView.findViewById<TextView>(R.id.text_date)
        val textStart = holder.itemView.findViewById<TextView>(R.id.text_start)
        val textEnd = holder.itemView.findViewById<TextView>(R.id.text_end)
        val textDuration = holder.itemView.findViewById<TextView>(R.id.text_duration)
        holder.itemView.apply {
            textUser.text = historyItems[position].nameUser
            textProject.text = historyItems[position].nameProject
            textDate.text = historyItems[position].date
            textStart.text = historyItems[position].sessionStart
            textEnd.text = historyItems[position].sessionEnd
            textDuration.text = historyItems[position].duration
        }
    }


}