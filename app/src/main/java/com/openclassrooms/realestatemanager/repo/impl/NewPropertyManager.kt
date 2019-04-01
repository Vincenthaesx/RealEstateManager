package com.openclassrooms.realestatemanager.repo.impl

import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.repo.NewPropertyRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

object NewPropertyManager : NewPropertyRepository {

    override fun addNewProperty(property: Property): Observable<Long> {
        return Observable.fromCallable {
            RealEstateDatabase.realEstateDatabase.propertyDao().insertNewProperty(property)
        }
                .subscribeOn(Schedulers.io())
    }

}