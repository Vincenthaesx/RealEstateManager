package com.openclassrooms.realestatemanager.models

import androidx.room.*
import io.reactivex.Single
import java.util.*

@Entity
data class Property(
        @PrimaryKey(autoGenerate = true)
        var pid: Int,                                 // Property ID generated randomly

        var type: String,                             // Type (apartment, loft, mansion, etc...)
        var address: String,                          // Full address of the property
        var price: String,                            // Price (in US Dollars)
        var surface: String,                          // Surface (in square meters)
        var roomsCount: Int,                          // Rooms count
        var bathroomsCount: Int,                      // Bathrooms count
        var bedroomsCount: Int,                       // Bedrooms count
        var description: String,                      // Full description of the property
        var pictureList: List<String>,                // List of pictures
        var descriptionPictureList: List<String>,     // List of description for pictures
        var status: Boolean = true,                   // Status (True is available, False is sold)
        var entryDate: Date,                         // Date of entry on the market
        var saleDate: Date? = null,                   // Date of sale, if sold
        var agent: String                             // Full name of the real estate agent in charge of this property
)

@Dao
interface PropertyDao {

    @Query("Select * FROM Property")
    fun getAllProperty(): Single<List<Property>>

    @Query("Select * FROM Property WHERE pid = :id")
    fun getProperty(id: Int): Single<Property>

    @Insert
    fun insertNewProperty(property: Property): Long

    @Update
    fun updateProperty(property: Property): Int

    @Delete
    fun deleteProperty(deleteProperty: Property): Int

}