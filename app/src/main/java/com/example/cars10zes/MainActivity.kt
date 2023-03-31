package com.example.cars10zes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.toKotlinDuration
import kotlin.time.Duration as Duration_kt
import com.example.cars10zes.BuildConfig



//TODO local sqlite db
// feature to add manuel entries
// feature to view history


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // navigation menu start
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        val historyFragment = HistoryFragment()
        val calendarFragment = CalendarFragment()
        val overviewFragment = OverviewFragment()
        val startEndTimesFragment = StartEndTimesFragment()
        val newEntryFragment = NewEntryFragment()
        val deleteEntryFragment = DeleteEntryFragment()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.nav_home -> replaceFragment(homeFragment, getString(R.string.home_fragment_title))
                R.id.nav_settings -> replaceFragment(settingsFragment, it.title.toString())
                R.id.nav_history -> replaceFragment(historyFragment, it.title.toString())

                R.id.nav_version -> Toast.makeText(applicationContext, BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show()
                R.id.nav_link -> openWebPage(getString(R.string.source_code_link))
            }
            true
        }
        // navigation menu end

        replaceFragment(homeFragment, getString(R.string.home_fragment_title))
        title = getString(R.string.home_fragment_title)
    }

    fun openWebPage(urls: String) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        startActivity(intents)
    }

    private fun replaceFragment(fragment: Fragment, title: String){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            addToBackStack(null)
            commit()
        }
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
            }
        return super.onOptionsItemSelected(item)
    }
}