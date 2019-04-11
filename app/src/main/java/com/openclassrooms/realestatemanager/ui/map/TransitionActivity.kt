package com.openclassrooms.realestatemanager.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.property.PropertyDetailFragment

class TransitionActivity : AppCompatActivity() {

    private var idProperty = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        idProperty = intent.getIntExtra(ID_PROPERTY, 0)

        if (idProperty != 0) {
            launchFragment()
        }

        configureToolBar()
    }

    // Fragment
    private fun launchFragment() {
        val propertyDetailFragment = PropertyDetailFragment()

        val bundle = Bundle()
        bundle.putInt(ID, idProperty)
        propertyDetailFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout_map, propertyDetailFragment)
                .commit()
    }

    // CONFIGURATION
    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.mapToolbar)
        setSupportActionBar(toolbar)
    }

    companion object {
        private const val ID_PROPERTY = "idProperty"
        private const val ID = "id"
    }
}
