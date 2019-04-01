package com.openclassrooms.realestatemanager.repo

import com.openclassrooms.realestatemanager.models.Property
import io.reactivex.Observable

interface PropertyRepository {

    fun getPropertyList(): Observable<List<Property>>

    fun addNewProperty(property: Property): Observable<Long>

    fun updateProperty(updateProperty: Property): Observable<Int>

    fun getProperty(id: Int): Observable<Property>

}