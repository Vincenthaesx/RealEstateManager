package com.openclassrooms.realestatemanager.repo.impl

import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.repo.UpdatePropertyRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object UpdatePropertyManager : UpdatePropertyRepository {

    override fun getProperty(id: Int): Observable<Property> {
        return RealEstateDatabase.realEstateDatabase.propertyDao().getProperty(id)
                .toObservable()
                .subscribeOn(Schedulers.io())
    }

    override fun updateProperty(updateProperty: Property): Observable<Int> {
        return Observable.fromCallable {
            RealEstateDatabase.realEstateDatabase.propertyDao().updateProperty(updateProperty)
        }
                .subscribeOn(Schedulers.io())
    }
}