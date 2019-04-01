package com.openclassrooms.realestatemanager.repo.impl

import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.repo.PropertyRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object PropertyManager : PropertyRepository {

    override fun getProperty(id: Int): Observable<Property> {
        return RealEstateDatabase.realEstateDatabase.propertyDao().getProperty(id)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateProperty(updateProperty: Property): Observable<Int> {
        return Observable.fromCallable {
            RealEstateDatabase.realEstateDatabase.propertyDao().updateProperty(updateProperty)
        }
                .subscribeOn(Schedulers.io())
    }

    override fun addNewProperty(property: Property): Observable<Long> {
        return Observable.fromCallable {
            RealEstateDatabase.realEstateDatabase.propertyDao().insertNewProperty(property)
        }
                .subscribeOn(Schedulers.io())
    }

    override fun getPropertyList(): Observable<List<Property>> {
        return RealEstateDatabase.realEstateDatabase.propertyDao().getAllProperty()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}