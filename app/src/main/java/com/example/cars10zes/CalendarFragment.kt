package com.example.cars10zes

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private lateinit var textCalendarMonth: TextView
    private lateinit var buttonCalendarPrevMonth: ImageButton
    private lateinit var buttonCalendarNextMonth: ImageButton
    private lateinit var spinnerProjects: Spinner
    private lateinit var timeTracking: TimeTracking

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        this.activity?.title = getString(R.string.title_calendar)

        timeTracking = TimeTracking(requireActivity().applicationContext)
        timeTracking.selectedProject = timeTracking.getLastProject()

        buttonCalendarPrevMonth = view.findViewById(R.id.button_calendar_prev_month)
        buttonCalendarNextMonth = view.findViewById(R.id.button_calendar_next_month)
        textCalendarMonth = view.findViewById(R.id.text_calendar_month)

        buttonCalendarPrevMonth.setOnClickListener {
            timeTracking.selectedDate = timeTracking.selectedDate.minusMonths(1)
            setMonthView()
        }

        buttonCalendarNextMonth.setOnClickListener {
            timeTracking.selectedDate = timeTracking.selectedDate.plusMonths(1)
            setMonthView()
        }

        spinnerProjects = view.findViewById(R.id.spinner_cal_project)
        val adapter = this.context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                timeTracking.getProjectsList())
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProjects.adapter = adapter


        spinnerProjects.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                timeTracking.selectedProject = adapterView?.getItemAtPosition(position).toString()
                setMonthView()
            }
        }

        setMonthView()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        textCalendarMonth.text = timeTracking.getCalendarDate()

        val daysOfMonth = timeTracking.getDaysOfMonth()
        val adapter = CalendarAdapter(daysOfMonth)

        val recyclerCalendar = view?.findViewById<RecyclerView>(R.id.recyclerviewCalendar)
        if (recyclerCalendar != null) {
            recyclerCalendar.adapter = adapter
            recyclerCalendar.layoutManager = GridLayoutManager(context, 7)
        }
    }
}