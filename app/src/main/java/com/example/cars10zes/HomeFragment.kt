package com.example.cars10zes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText


class HomeFragment : Fragment(R.layout.fragment_home) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        this.activity?.title = getString(R.string.title_home)

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

        buttonSessionStart.setOnClickListener {
            val userName = inputUser.text.toString()
            val projectName = inputProject.text.toString()
            if (userName.isNotEmpty() && projectName.isNotEmpty()) {
                timeTracking.startSession(userName, projectName)
                textSessionStartTime.text = timeTracking.getSessionStartTime()
                buttonSessionEnd.isEnabled = true
                buttonPauseStart.isEnabled = true
                buttonPauseEnd.isEnabled = false
                buttonSessionStart.isEnabled = false
            } else {
                Toast.makeText(context, R.string.toast_home_missing_data, Toast.LENGTH_LONG).show()
            }
        }

        buttonSessionEnd.setOnClickListener {
            timeTracking.endSession()
            textSessionEndTime.text = timeTracking.getSessionEndTime()
            textSessionDiffTime.text = timeTracking.getSessionDuration()
            textGesTime.text = timeTracking.getSessionWorkingTime()
            buttonSessionEnd.isEnabled = false
            buttonPauseStart.isEnabled = false
            buttonPauseEnd.isEnabled = false
            buttonSessionStart.isEnabled = true
        }

        buttonPauseStart.setOnClickListener {
            timeTracking.startPause()
            textPauseStartTime.text = timeTracking.getPauseStartTime()
            buttonSessionEnd.isEnabled = false
            buttonPauseStart.isEnabled = false
            buttonPauseEnd.isEnabled = true
            buttonSessionStart.isEnabled = false
        }

        buttonPauseEnd.setOnClickListener {
            timeTracking.endPause()
            textPauseEndTime.text =  timeTracking.getPauseEndTime()
            textPauseDiffTime.text = timeTracking.getPauseDuration()
            buttonSessionEnd.isEnabled = true
            buttonPauseStart.isEnabled = true
            buttonPauseEnd.isEnabled = false
            buttonSessionStart.isEnabled = false
        }

        // restore last state
        inputUser.setText(timeTracking.getLastUser())
        inputProject.setText(timeTracking.getLastProject())
        timeTracking.restoreStatus()
        when(timeTracking.status){
            0 -> {
                buttonSessionStart.isEnabled = true
                buttonSessionEnd.isEnabled = false
                buttonPauseStart.isEnabled = false
                buttonPauseEnd.isEnabled = false
            }
            1 -> {
                textSessionStartTime.text = timeTracking.getSessionStartTime()
                buttonSessionStart.isEnabled = false
                buttonSessionEnd.isEnabled = true
                buttonPauseStart.isEnabled = true
                buttonPauseEnd.isEnabled = false
            }
            2 -> {
                textPauseStartTime.text = timeTracking.getPauseStartTime()
                buttonSessionEnd.isEnabled = false
                buttonPauseStart.isEnabled = false
                buttonPauseEnd.isEnabled = true
                buttonSessionStart.isEnabled = false
                textSessionStartTime.text = timeTracking.getSessionStartTime()
            }
            3 -> {
                textPauseEndTime.text =  timeTracking.getPauseEndTime()
                textPauseDiffTime.text = timeTracking.getPauseDuration()
                buttonSessionEnd.isEnabled = true
                buttonPauseStart.isEnabled = true
                buttonPauseEnd.isEnabled = false
                buttonSessionStart.isEnabled = false
                textSessionStartTime.text = timeTracking.getSessionStartTime()
                textPauseStartTime.text = timeTracking.getPauseStartTime()
            }
            4 -> {
                textSessionEndTime.text = timeTracking.getSessionEndTime()
                textSessionDiffTime.text = timeTracking.getSessionDuration()
                textGesTime.text = timeTracking.getSessionWorkingTime()
                buttonSessionEnd.isEnabled = false
                buttonPauseStart.isEnabled = false
                buttonPauseEnd.isEnabled = false
                buttonSessionStart.isEnabled = true
                textSessionStartTime.text = timeTracking.getSessionStartTime()
                textPauseStartTime.text = timeTracking.getPauseStartTime()
                textPauseEndTime.text =  timeTracking.getPauseEndTime()
                textPauseDiffTime.text = timeTracking.getPauseDuration()
            }
        }
    }
}
