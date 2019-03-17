package com.openclassrooms.realestatemanager.ui.main

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.property.NewProperty
import com.openclassrooms.realestatemanager.ui.property.PropertyFragment
import com.openclassrooms.realestatemanager.utils.addFragment
import com.openclassrooms.realestatemanager.utils.openActivity
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private val disposable : CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private lateinit var propertyFragment: PropertyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureToolBar()

        propertyFragment = PropertyFragment()
        addFragment(propertyFragment, R.id.activity_main_frame_property)
    }

    override fun onDestroy() {
        disposeWhenDestroy()
        super.onDestroy()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    // -------------
    // TOOLBAR
    // -------------

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        menu?.clear()
        inflater.inflate(R.menu.activity_main_menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.buttonAdd -> {
               openActivity<NewProperty>()
            }
            R.id.buttonSearch -> {

            }
        }
        return true
    }

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

}
