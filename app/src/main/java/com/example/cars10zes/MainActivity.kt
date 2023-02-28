package com.example.cars10zes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.toKotlinDuration
import kotlin.time.Duration as Duration_kt


//TODO local sqlite db
// feature to add manuel entries
// feature to view history


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var start_datetime = LocalDateTime.now()
        var end_datetime: LocalDateTime
        var start_pause_datetime = LocalDateTime.now()
        var end_pause_datetime: LocalDateTime

        val pause_init = Duration.between(start_pause_datetime, start_pause_datetime)
        var sec_pause = pause_init.seconds
        val formatter = DateTimeFormatter.ofPattern("HH:mm") // "yyyy-MM-dd HH:mm:ss.SSS"
        val sec_pro_min = 60
        val sec_pro_hour = 60*sec_pro_min
        //var formatted = start_datetime.format(formatter)
        //println("Current Date and Time is: ${start_datetime.format(formatter)}")

        val button_session_start = findViewById<Button>(R.id.button_session_start)
        val button_session_end = findViewById<Button>(R.id.button_session_end)

        val button_pause_start = findViewById<Button>(R.id.button_pause_start)
        val button_pause_end = findViewById<Button>(R.id.button_pause_end)

        val input_user = findViewById<TextInputEditText>(R.id.input_user)
        val input_project = findViewById<TextInputEditText>(R.id.input_project)

        val text_session_start_time = findViewById<TextView>(R.id.text_session_start_time)
        val text_session_end_time = findViewById<TextView>(R.id.text_session_end_time)
        val text_session_diff_time = findViewById<TextView>(R.id.text_session_diff_time)
        val text_pause_start_time = findViewById<TextView>(R.id.text_pause_start_time)
        val text_pause_end_time = findViewById<TextView>(R.id.text_pause_end_time)
        val text_pause_diff_time = findViewById<TextView>(R.id.text_pause_diff_time)

        val text_ges_time = findViewById<TextView>(R.id.text_ges_time)

        button_session_end.isEnabled = false
        button_pause_start.isEnabled = false
        button_pause_end.isEnabled = false

        button_session_start.setOnClickListener {
            start_datetime = LocalDateTime.now()
            //println("Current Date and Time is: ${start_datetime.format(formatter)}")
            text_session_start_time.text = start_datetime.format(formatter)

            button_session_end.isEnabled = true
            button_pause_start.isEnabled = true
            button_pause_end.isEnabled = false
            button_session_start.isEnabled = false
        }

        button_session_end.setOnClickListener {
            end_datetime = LocalDateTime.now()
            val diff = Duration.between(start_datetime, end_datetime)

            var s = diff.seconds
            val h = s / sec_pro_hour;
            s -= h * sec_pro_hour;
            val m = s / sec_pro_min;
            s -= m * sec_pro_min;

            val diff_concat = "$h:$m:$s"
            text_session_diff_time.text = diff_concat
            text_session_end_time.text =  end_datetime.format(formatter)

            button_session_end.isEnabled = false
            button_pause_start.isEnabled = false
            button_pause_end.isEnabled = false
            button_session_start.isEnabled = true

            var sg = diff.seconds-sec_pause
            val hg = sg / sec_pro_hour;
            sg -= hg * sec_pro_hour;
            val mg = sg / sec_pro_min;
            sg -= mg * sec_pro_min;

            val ges_concat = "$hg:$mg:$sg"
            text_ges_time.text = ges_concat
        }

        button_pause_start.setOnClickListener {
            start_pause_datetime = LocalDateTime.now()
            text_pause_start_time.text = start_pause_datetime.format(formatter)

            button_session_end.isEnabled = false
            button_pause_start.isEnabled = false
            button_pause_end.isEnabled = true
            button_session_start.isEnabled = false
        }

        button_pause_end.setOnClickListener {
            end_pause_datetime = LocalDateTime.now()
            val diff = Duration.between(start_pause_datetime, end_pause_datetime)

            var s = diff.seconds
            sec_pause = s
            val h = s / sec_pro_hour;
            s -= h * sec_pro_hour;
            val m = s / sec_pro_min;
            s -= m * sec_pro_min;

            val diff_concat = "$h:$m:$s"
            text_pause_diff_time.text = diff_concat
            text_pause_end_time.text =  end_pause_datetime.format(formatter)

            button_session_end.isEnabled = true
            button_pause_start.isEnabled = false
            button_pause_end.isEnabled = false
            button_session_start.isEnabled = false
        }
    }
}