package com.openclassrooms.realestatemanager.models

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

@Entity
data class Property(
        @PrimaryKey (autoGenerate = true)
        var pid: Int, // Property ID generated randomly

        var type: String, // Type (apartment, loft, mansion, etc...)
        var address: String, // Full address of the property
        var price: Int, // Price (in US Dollars)
        var surface: Int, // Surface (in square meters)
        var roomsCount: Int, // Rooms count
        var description: String, // Full description of the property
        @TypeConverters(ListTypeConverter::class)
        var picturesList: List<String>, // List of pictures urls
        var status: Boolean, // Status (True is available, False is sold)
        @TypeConverters(DateTypeConverter::class)
        var entryDate: Date, // Date of entry on the market
        var saleDate: Date? = null, // Date of sale, if sold
        var agent: String // Full name of the real estate agent in charge of this property
)

@Dao
interface PropertyDao {

    @Query("Select * FROM Property")
    fun getAllProperty(): Single<List<Property>>

    @Insert
    fun insertNewProperty(property: Property) : Long

    @Delete
    fun deleteProperty(deleteProperty: Property) : Int

}