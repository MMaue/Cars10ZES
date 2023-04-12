package com.example.cars10zes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class OverviewFragment : Fragment(R.layout.fragment_overview) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        this.activity?.title = getString(R.string.title_overview)

        val timeTracking = TimeTracking(requireActivity().applicationContext)

        val adapter = OverviewAdapter(timeTracking.getOverviewList())
        val recyclerOverview = view.findViewById<RecyclerView>(R.id.recyclerviewOverview)
        recyclerOverview.adapter = adapter
        recyclerOverview.layoutManager = LinearLayoutManager(this.activity)
    }
}