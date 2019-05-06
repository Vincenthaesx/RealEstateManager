package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.utils.toFRDate
import io.reactivex.Single
import java.util.*

@Entity
data class Property(
        @PrimaryKey(autoGenerate = true)
        var pid: Int,                                 // Property ID generated randomly

        var type: String,                             // Type (apartment, loft, mansion, etc...)
        var address: String,                          // Full address of the property
        var price: Int,                               // Price (in US Dollars)
        var surface: Int,                             // Surface (in square meters)
        var roomsCount: Int,                          // Rooms count
        var bathroomsCount: Int,                      // Bathrooms count
        var bedroomsCount: Int,                       // Bedrooms count
        var description: String,                      // Full description of the property
        var PointOfInterest: String,                  // Point of interest
        var pictureList: List<String>,                // List of pictures
        var descriptionPictureList: List<String>,     // List of description for pictures
        var status: String,                           // Status (Available or Sold)
        var entryDate: Date,                          // Date of entry on the market
        var saleDate: Date? = null,                   // Date of sale, if sold
        var agent: String                             // Full name of the real estate agent in charge of this property
) {
    constructor() : this(0, "", "", 0, 0,
            0, 0, 0, "", "",
            arrayListOf("null"), arrayListOf("null"), "Disponible", Date(), null, "")
}

fun fromContentValues(values: ContentValues): Property {
    val property = Property()
    if (values.containsKey("type")) property.type = values.getAsString("type")
    if (values.containsKey("address")) property.address = values.getAsString("address")
    if (values.containsKey("price")) property.price = values.getAsInteger("price")
    if (values.containsKey("surface")) property.surface = values.getAsInteger("surface")
    if (values.containsKey("roomsCount")) property.roomsCount = values.getAsInteger("roomsCount")
    if (values.containsKey("bathroomsCount")) property.bathroomsCount = values.getAsInteger("bathroomsCount")
    if (values.containsKey("bedroomsCount")) property.bedroomsCount = values.getAsInteger("bedroomsCount")
    if (values.containsKey("pictureList")) property.pictureList = listOf(values.getAsString("pictureList"))
    if (values.containsKey("descriptionPictureList")) property.descriptionPictureList = listOf(values.getAsString("descriptionPictureList"))
    if (values.containsKey("description")) property.description = values.getAsString("description")
    if (values.containsKey("PointOfInterest")) property.PointOfInterest = values.getAsString("PointOfInterest")
    if (values.containsKey("status")) property.status = values.getAsString("status")
    if (values.containsKey("entryDate")) property.entryDate = values.getAsLong("entryDate").toFRDate()
    if (values.containsKey("saleDate")) property.saleDate = values.getAsLong("saleDate").toFRDate()
    if (values.containsKey("agent")) property.agent = values.getAsString("agent")

    Log.e("EstateFromContentValues", "Property : $property")
    return property
}

@Dao
interface PropertyDao {

    @Query("Select * FROM Property")
    fun getAllProperty(): Single<List<Property>>

    @Query("SELECT * FROM Property WHERE pid = :id")
    fun getItemsWithCursor(id: Long): Cursor

    @Query("Select * FROM Property WHERE pid = :id")
    fun getProperty(id: Int): Single<Property>

    @RawQuery
    fun getItemsBySearch(query: SupportSQLiteQuery): Single<List<Property>>

    @Insert
    fun insertNewProperty(property: Property): Long

    @Update
    fun updateProperty(property: Property): Int

    @Delete
    fun deleteProperty(deleteProperty: Property): Int

}