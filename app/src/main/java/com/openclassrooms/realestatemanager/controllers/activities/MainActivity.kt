package com.openclassrooms.realestatemanager.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDatabaseProperty()

    }

    private fun getDatabaseProperty(): Observable<List<Property>> {
        return RealEstateDatabase.realEstateDatabase.propertyDao().getAllProperty()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
