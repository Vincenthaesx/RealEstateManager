package com.openclassrooms.realestatemanager.ui.newProperty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.addFragment
import io.reactivex.disposables.CompositeDisposable

class NewProperty : AppCompatActivity() {

    private val disposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var survey1: FragmentSurvey1 = FragmentSurvey1()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_property)

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
