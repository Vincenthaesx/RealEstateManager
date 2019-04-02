package com.openclassrooms.realestatemanager.ui.updateProperty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import io.reactivex.disposables.CompositeDisposable

class UpdateProperty : AppCompatActivity() {

    private val disposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var surveyUpdate1: FragmentUpdateSurvey1 = FragmentUpdateSurvey1()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_property)

        val idProperty = intent.getIntExtra(ID_PROPERTY, 0)

        val bundle = Bundle()
        bundle.putInt(ID_PROPERTY_BUNDLE, idProperty)

        surveyUpdate1.arguments = bundle
        supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout_updateProperty, surveyUpdate1)
                .commit()

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
        val toolbar = findViewById<Toolbar>(R.id.updatePropertyToolbar)
        setSupportActionBar(toolbar)
    }

    // ---------------

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

    companion object {
        private const val ID_PROPERTY_BUNDLE = "propertyId"
        private const val ID_PROPERTY = "idProperty"
    }

}

