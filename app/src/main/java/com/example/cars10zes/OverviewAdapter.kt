package com.example.cars10zes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

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

            val textMax = holder.itemView.findViewById<TextView>(R.id.text_project_duration_max)
            val textMin = holder.itemView.findViewById<TextView>(R.id.text_project_duration_min)
            val textAverage = holder.itemView.findViewById<TextView>(R.id.text_project_duration_avg)

            val timeDurationBarChart = holder.itemView.findViewById<BarChart>(R.id.time_duration_bar_chart)

            holder.itemView.apply {
                textProject.text = overviewItems[position].nameProject
                textDuration.text = overviewItems[position].projectDuration

                var max = overviewItems[position].projectDurations[0].sessionDuration-overviewItems[position].projectDurations[0].sessionPauseDuration
                var min = max
                var average = max

                val barChartEntries = mutableListOf<BarEntry>()
                for ((i, entry) in overviewItems[position].projectDurations.withIndex()) {
                    val sessionDuration = entry.sessionDuration-entry.sessionPauseDuration
                    barChartEntries.add(BarEntry(i.toFloat(), floatArrayOf(
                        entry.sessionPauseDuration.toFloat()/3600,
                        sessionDuration.toFloat()/3600)))
                    if (max < sessionDuration) {
                        max = sessionDuration
                    }
                    if (min > sessionDuration) {
                        min = sessionDuration
                    }
                    average += sessionDuration
                }
                average /= overviewItems[position].projectDurations.size

                textMax.text = formatDuration(max)
                textMin.text = formatDuration(min)
                textAverage.text = formatDuration(average)

                val barDataSet = BarDataSet(barChartEntries, "")

                barDataSet.setColors(listOf(
                    ContextCompat.getColor(context, R.color.text_negative),
                    ContextCompat.getColor(context, R.color.blue_1),
                    ContextCompat.getColor(context, R.color.text_negative),
                    ContextCompat.getColor(context, R.color.blue_2),
                    ContextCompat.getColor(context, R.color.text_negative),
                    ContextCompat.getColor(context, R.color.blue_3)))

                val data = BarData(barDataSet)
                timeDurationBarChart.data = data

                val xAxis: XAxis = timeDurationBarChart.xAxis

                timeDurationBarChart.axisLeft.setDrawGridLines(false)
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)

                timeDurationBarChart.legend.isEnabled = false
                timeDurationBarChart.description.isEnabled = false

                timeDurationBarChart.animateY(2500)
                timeDurationBarChart.invalidate()
            }
        }

    private fun formatDuration(seconds: Int): String {
        var s = seconds
        val h = s / 3600;
        s -= h * 3600;
        val m = s / 60;
        s -= m * 60;
        return "$h:$m:$s"
    }

    }