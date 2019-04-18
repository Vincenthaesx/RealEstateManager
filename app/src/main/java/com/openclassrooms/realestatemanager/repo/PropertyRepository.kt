package com.openclassrooms.realestatemanager.repo

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.models.Property
import io.reactivex.Observable

interface PropertyRepository {

    fun getPropertyList(): Observable<List<Property>>

    fun addNewProperty(property: Property): Observable<Long>

    fun getProperty(id: Int): Observable<Property>

    fun getPropertyBySearch(query: SimpleSQLiteQuery) : Observable<List<Property>>
}