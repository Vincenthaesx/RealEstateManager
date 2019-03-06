package com.openclassrooms.realestatemanager.repo

import com.openclassrooms.realestatemanager.models.Property
import io.reactivex.Observable

interface PropertyRepository {

    fun getPropertyList(): Observable<List<Property>>

}