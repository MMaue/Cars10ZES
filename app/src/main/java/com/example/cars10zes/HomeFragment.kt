package com.example.cars10zes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText


class HomeFragment : Fragment(R.layout.fragment_home) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val timeTracking = arguments?.get("data") as TimeTracking

        val buttonSessionStart = view.findViewById<Button>(R.id.button_session_start)
        val buttonSessionEnd = view.findViewById<Button>(R.id.button_session_end)

        val buttonPauseStart = view.findViewById<Button>(R.id.button_pause_start)
        val buttonPauseEnd = view.findViewById<Button>(R.id.button_pause_end)

        val inputUser = view.findViewById<TextInputEditText>(R.id.input_user)
        val inputProject = view.findViewById<TextInputEditText>(R.id.input_project)

        val textSessionStartTime = view.findViewById<TextView>(R.id.text_session_start_time)
        val textSessionEndTime = view.findViewById<TextView>(R.id.text_session_end_time)
        val textSessionDiffTime = view.findViewById<TextView>(R.id.text_session_diff_time)
        val textPauseStartTime = view.findViewById<TextView>(R.id.text_pause_start_time)
        val textPauseEndTime = view.findViewById<TextView>(R.id.text_pause_end_time)
        val textPauseDiffTime = view.findViewById<TextView>(R.id.text_pause_diff_time)

        val textGesTime = view.findViewById<TextView>(R.id.text_ges_time)

        buttonSessionEnd.isEnabled = false
        buttonPauseStart.isEnabled = false
        buttonPauseEnd.isEnabled = false

        buttonSessionStart.setOnClickListener {
            textSessionStartTime.text = timeTracking.startSession()
            buttonSessionEnd.isEnabled = true
            buttonPauseStart.isEnabled = true
            buttonPauseEnd.isEnabled = false
            buttonSessionStart.isEnabled = false
        }

        buttonSessionEnd.setOnClickListener {
            textSessionEndTime.text = timeTracking.endSession()
            textSessionDiffTime.text = timeTracking.getSessionDuration()
            textGesTime.text = timeTracking.getSessionWorkingTime()
            buttonSessionEnd.isEnabled = false
            buttonPauseStart.isEnabled = false
            buttonPauseEnd.isEnabled = false
            buttonSessionStart.isEnabled = true
        }

        buttonPauseStart.setOnClickListener {
            textPauseStartTime.text = timeTracking.startPause()
            buttonSessionEnd.isEnabled = false
            buttonPauseStart.isEnabled = false
            buttonPauseEnd.isEnabled = true
            buttonSessionStart.isEnabled = false
        }

        buttonPauseEnd.setOnClickListener {
            textPauseEndTime.text =  timeTracking.endPause()
            textPauseDiffTime.text = timeTracking.getPauseDuration()
            buttonSessionEnd.isEnabled = true
            buttonPauseStart.isEnabled = false
            buttonPauseEnd.isEnabled = false
            buttonSessionStart.isEnabled = false
        }
    }
}
