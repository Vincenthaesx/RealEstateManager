package com.openclassrooms.realestatemanager.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R

class SearchTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_transaction)

        configureToolBar()

        val searchFragment = SearchFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_search, searchFragment)
                .commit()
    }

    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
}
