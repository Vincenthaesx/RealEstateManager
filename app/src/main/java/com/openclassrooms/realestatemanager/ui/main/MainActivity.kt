package com.openclassrooms.realestatemanager.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.createNewProperty.NewProperty
import com.openclassrooms.realestatemanager.ui.property.PropertyFragment
import com.openclassrooms.realestatemanager.utils.addFragment
import com.openclassrooms.realestatemanager.utils.openActivity
import io.reactivex.disposables.CompositeDisposable


class MainActivity : AppCompatActivity() {

    private val disposable : CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var propertyFragment: PropertyFragment = PropertyFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        configureToolBar()

        when {
            resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> {
                if (propertyFragment.isVisible) {
                    addFragment(propertyFragment, R.id.activity_main_frame_property)
                } else {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_frame_property, propertyFragment)
                            .commit()
                }
            }
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> {
                if (propertyFragment.isVisible) {
                    addFragment(propertyFragment, R.id.activity_main_frame_property)
                } else {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_frame_property, propertyFragment)
                            .commit()
                }
            }
            else -> Log.e("TAG", "Error")
        }
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

    // --------------
    // PERMISSIONS
    // --------------

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
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
        private const val PERMISSION_REQUEST_CODE: Int = 101
    }

}
