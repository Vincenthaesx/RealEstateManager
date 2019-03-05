package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.RealEstateManagerApplication

@Database(entities = [Property::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class, ListTypeConverter::class)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    companion object {
        val realEstateDatabase by lazy {
            Room.databaseBuilder(
                    RealEstateManagerApplication.applicationContext(),
                    RealEstateDatabase::class.java,
                    "Word_database"
            )
                    .addCallback(prepopulateDatabase())
                    .build()
        }

        private fun  prepopulateDatabase(): RoomDatabase.Callback {

            return object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {

                    super.onCreate(db)

                    val contentValues = ContentValues()

                    contentValues.put("type", "House")
                    contentValues.put("address", "7 Rue aux Moutons, 28270 Brezolles")
                    contentValues.put("price", 235000)
                    contentValues.put("surface", 107)
                    contentValues.put("roomsCount", 6)
                    contentValues.put("description", "A nice test house to see if everything works, thanks a lot")
                    contentValues.put("pictureList", "https://v.seloger.com/s/crop/310x225/visuels/0/m/l/4/0ml42xbt1n3itaboek3qec5dtskdgw6nlscu7j69k.jpg")
                    contentValues.put("status", true)
                    contentValues.put("entryDate", 2018-12-18)
                    contentValues.put("saleDate", "")
                    contentValues.put("agent", "Vincent")

                    db.insert("property", OnConflictStrategy.REPLACE, contentValues)
                }
            }
        }


    }
}

