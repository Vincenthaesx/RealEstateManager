package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
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
                    .addCallback(prepopulateDatabase())
                    .build()
        }

        private fun prepopulateDatabase(): RoomDatabase.Callback {

            return object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {

                    super.onCreate(db)

                    val contentValues = ContentValues()
                    val contentValues1 = ContentValues()
                    val list1 = listOf("https://v.seloger.com/s/crop/310x225/visuels/0/m/l/4/0ml42xbt1n3itaboek3qec5dtskdgw6nlscu7j69k.jpg", "https://t-ec.bstatic.com/images/hotel/max1024x768/112/112064745.jpg", "https://v.seloger.com/s/crop/310x225/visuels/0/m/l/4/0ml42xbt1n3itaboek3qec5dtskdgw6nlscu7j69k.jpg", "https://v.seloger.com/s/crop/310x225/visuels/0/m/l/4/0ml42xbt1n3itaboek3qec5dtskdgw6nlscu7j69k.jpg", "https://v.seloger.com/s/crop/310x225/visuels/0/m/l/4/0ml42xbt1n3itaboek3qec5dtskdgw6nlscu7j69k.jpg")
                    val list2 = listOf("https://t-ec.bstatic.com/images/hotel/max1024x768/112/112064745.jpg")

                    val list3 = listOf("home", "palace", "test", "test2", "test3")
                    val list4 = listOf("palace")
                    val gson = Gson()

                    contentValues.put("type", "House")
                    contentValues.put("address", "2 Rue aux Moutons 28270 Brezolles")
                    contentValues.put("price", "$256 976")
                    contentValues.put("surface", "107 m²")
                    contentValues.put("roomsCount", 6)
                    contentValues.put("bathroomsCount", 1)
                    contentValues.put("bedroomsCount", 2)
                    contentValues.put("description", "A nice background_property house to see if everything works, la description de la maison dois normalement s'afficher correctement sans poser de soucis ni en portrait ni en paysage, ce qui permettra a l'utilisateur de profiter un maximum de l'application.")
                    contentValues.put("pictureList", gson.toJson(list1))
                    contentValues.put("descriptionPictureList", gson.toJson(list3))
                    contentValues.put("status", true)
                    contentValues.put("entryDate", 2018-5-28)
                    contentValues.put("saleDate", "")
                    contentValues.put("agent", "Vincent")
                    // -------------------
                    contentValues1.put("type", "Palace")
                    contentValues1.put("address", "24 Rue Saint-Dominique 75007 Paris")
                    contentValues1.put("price", "$552 000")
                    contentValues1.put("surface", "185 m²")
                    contentValues1.put("roomsCount", 12)
                    contentValues1.put("bathroomsCount", 2)
                    contentValues1.put("bedroomsCount", 4)
                    contentValues1.put("description", "A nice background_property palace to see if everything works, thanks a lot")
                    contentValues1.put("pictureList", gson.toJson(list2))
                    contentValues1.put("descriptionPictureList", gson.toJson(list4))
                    contentValues1.put("status", true)
                    contentValues1.put("entryDate", 2018-5-28)
                    contentValues1.put("saleDate", "")
                    contentValues1.put("agent", "Vincent")

                    db.insert("property", OnConflictStrategy.REPLACE, contentValues)
                    db.insert("property", OnConflictStrategy.REPLACE, contentValues1)
                }
            }
        }


    }
}

