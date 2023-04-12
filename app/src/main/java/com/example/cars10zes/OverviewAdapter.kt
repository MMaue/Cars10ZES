package com.example.cars10zes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OverviewAdapter(
    var overviewItems: List<OverviewItem>
    ) : RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder>() {
        inner class OverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_overview, parent, false)
            return OverviewViewHolder(view)
        }

        override fun getItemCount(): Int {
            return overviewItems.size
        }

        override fun onBindViewHolder(holder: OverviewViewHolder, position: Int) {
            val textProject = holder.itemView.findViewById<TextView>(R.id.text_project_overview)
            val textDuration = holder.itemView.findViewById<TextView>(R.id.text_project_duration)
            holder.itemView.apply {
                textProject.text = overviewItems[position].nameProject
                textDuration.text = overviewItems[position].projectDuration
            }
        }


    }