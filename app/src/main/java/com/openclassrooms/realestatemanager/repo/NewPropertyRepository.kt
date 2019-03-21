package com.openclassrooms.realestatemanager.repo

import com.openclassrooms.realestatemanager.models.Property
import io.reactivex.Observable

interface NewPropertyRepository {

    fun addNewProperty(property: Property): Observable<Long>

    fun updateProperty(updateProperty: Property): Observable<Int>

}