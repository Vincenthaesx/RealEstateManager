package com.openclassrooms.realestatemanager.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R

class MapTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_transaction)

        configureToolBar()

        val mapFragment = MapFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_map, mapFragment)
                .commit()
    }

    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
}
