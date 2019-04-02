package com.openclassrooms.realestatemanager.repo

import com.openclassrooms.realestatemanager.models.Property
import io.reactivex.Observable

interface UpdatePropertyRepository {

    fun updateProperty(updateProperty: Property): Observable<Int>

    fun getProperty(id: Int): Observable<Property>
}