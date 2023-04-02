package com.example.cars10zes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HistoryFragment : Fragment(R.layout.fragment_history) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        this.activity?.title = getString(R.string.title_history)

        val timeTracking = arguments?.get("data") as TimeTracking

        val adapter = HistoryAdapter(timeTracking.getHistoryList())
        val recyclerHistory = view.findViewById<RecyclerView>(R.id.recyclerviewHistory)
        recyclerHistory.adapter = adapter
        recyclerHistory.layoutManager = LinearLayoutManager(this.activity)
    }
}
