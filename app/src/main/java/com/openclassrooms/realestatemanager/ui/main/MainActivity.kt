package com.openclassrooms.realestatemanager.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R

class MainActivity : AppCompatActivity() {

    private lateinit var propertyFragment: PropertyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureToolBar()
        configurePropertyFragment()

    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configurePropertyFragment() {
        propertyFragment = PropertyFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_frame_property, propertyFragment)
                .commit()
    }

}
