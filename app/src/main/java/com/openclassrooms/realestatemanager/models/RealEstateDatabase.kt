package com.openclassrooms.realestatemanager.models

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.RealEstateManagerApplication

@Database(entities = [Property::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    companion object {
        val realEstateDatabase by lazy {
            Room.databaseBuilder(
                    RealEstateManagerApplication.applicationContext(),
                    RealEstateDatabase::class.java,
                    "World_database"
            )
                    .build()
        }
    }
}

