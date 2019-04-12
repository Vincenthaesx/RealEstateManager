package com.openclassrooms.realestatemanager.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.material.navigation.NavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.property.MapFragment
import com.openclassrooms.realestatemanager.ui.property.PropertyFragment
import com.openclassrooms.realestatemanager.utils.addFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val disposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var propertyFragment: PropertyFragment = PropertyFragment()
    private var mapFragment: MapFragment = MapFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            if (!propertyFragment.isVisible) {
                addFragment(propertyFragment, R.id.activity_main_frame_property)
            } else {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_frame_property, propertyFragment)
                        .commit()
            }
        }

        requestPermission()
        configureToolBar()
        configureBottomView()
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

    private fun configureBottomView() {
        bottom_navigation.setOnNavigationItemSelectedListener { item -> onNavigationItemSelected(item) }
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
            R.id.buttonSearch -> {

            }
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list_view -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_frame_property, propertyFragment)
                        .commit()
            }
            R.id.action_map -> {
                requestPermissionMap()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_frame_property, mapFragment)
                        .commit()
            }
        }
        return true
    }

    // --------------
    // PERMISSIONS
    // --------------

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE_MAIN)
    }

    private fun requestPermissionMap() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE_MAIN -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            PERMISSION_REQUEST_CODE -> {

                if (grantResults.size > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
            }
        }
    }

    // ---------------

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 110
        private const val PERMISSION_REQUEST_CODE_MAIN: Int = 101
    }

}
