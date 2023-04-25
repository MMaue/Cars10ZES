package com.example.cars10zes

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private var calendarItems: ArrayList<CalendarItem>
    ) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
        view.layoutParams.height = parent.height / 6
        return CalendarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return calendarItems.size
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalendarViewHolder, position: Int) {
        val textCalendarCell = holder.itemView.findViewById<TextView>(R.id.calendar_cell_text)
        val cellBorderLeft = holder.itemView.findViewById<View>(R.id.view_calendar_cell_vertical_left)
        val cellBorderRight = holder.itemView.findViewById<View>(R.id.view_calendar_cell_vertical_right)
        val cellBorderTop = holder.itemView.findViewById<View>(R.id.view_calendar_cell_horizontal_top)
        val cellBorderBottom = holder.itemView.findViewById<View>(R.id.view_calendar_cell_horizontal_bottom)
        val constraintLayoutCalendarCell = holder.itemView.findViewById<ConstraintLayout>(R.id.calender_cell_constraint_layout)
        holder.itemView.apply {
            textCalendarCell.text = calendarItems[position].dayOfMonth
            if (calendarItems[position].sessionFound) {
                constraintLayoutCalendarCell.setBackgroundColor(resources.getColor(R.color.blue_2))
            }
            if (calendarItems[position].dayOfMonth == "") {
                cellBorderLeft.background = null
                cellBorderRight.background = null
                cellBorderTop.background = null
                cellBorderBottom.background = null
            }
        }
    }

    }