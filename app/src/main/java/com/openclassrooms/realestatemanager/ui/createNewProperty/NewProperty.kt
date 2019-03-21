package com.openclassrooms.realestatemanager.ui.createNewProperty

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.utils.addFragment
import io.reactivex.disposables.CompositeDisposable

class NewProperty : AppCompatActivity(){

    private val disposable : CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private lateinit var survey1: Survey1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_property)

        survey1 = Survey1()
        addFragment(survey1, R.id.frameLayout_newProperty)

        configureToolBar()
    }


    override fun onDestroy() {
        disposeWhenDestroy()
        super.onDestroy()
    }


    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.newPropertyToolbar)
        setSupportActionBar(toolbar)
    }

    // ---------------

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }



}
