package com.example.cars10zes

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // navigation menu start
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val timeTracking = TimeTracking(applicationContext)
        val bundle = Bundle()
        bundle.putSerializable("data", timeTracking)

        val homeFragment = HomeFragment()
        homeFragment.arguments = bundle
        val settingsFragment = SettingsFragment()
        settingsFragment.arguments = bundle
        val historyFragment = HistoryFragment()
        historyFragment.arguments = bundle
        val calendarFragment = CalendarFragment()
        calendarFragment.arguments = bundle
        val overviewFragment = OverviewFragment()
        overviewFragment.arguments = bundle
        val startEndTimesFragment = StartEndTimesFragment()
        startEndTimesFragment.arguments = bundle
        val newEntryFragment = NewEntryFragment()
        newEntryFragment.arguments = bundle
        val deleteEntryFragment = DeleteEntryFragment()
        deleteEntryFragment.arguments = bundle

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.nav_home -> replaceFragment(homeFragment)
                R.id.nav_settings -> replaceFragment(settingsFragment)
                R.id.nav_history -> replaceFragment(historyFragment)
                R.id.nav_calendar -> replaceFragment(calendarFragment)
                R.id.nav_overview -> replaceFragment(overviewFragment)
                R.id.nav_startendtimes -> replaceFragment(startEndTimesFragment)
                R.id.nav_entry -> replaceFragment(newEntryFragment)
                R.id.nav_trash -> replaceFragment(deleteEntryFragment)

                R.id.nav_version -> Toast.makeText(applicationContext, BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show()
                R.id.nav_link -> openWebPage(getString(R.string.source_code_link))
            }
            true
        }
        // navigation menu end
        replaceFragment(homeFragment)
    }

    private fun openWebPage(urls: String) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        startActivity(intents)
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            addToBackStack(null)
            commit()
        }
        drawerLayout.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
            }
        return super.onOptionsItemSelected(item)
    }
}