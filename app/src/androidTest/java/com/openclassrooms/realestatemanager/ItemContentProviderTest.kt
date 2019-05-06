package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.ui.provider.ItemContentProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ItemContentProviderTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    // DATA SET FOR TEST
    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        mContentResolver = InstrumentationRegistry.getContext().contentResolver
    }


    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor = mContentResolver?.query(ContentUris.withAppendedId(ItemContentProvider().URI_ESTATE, USER_ID), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(0, `is`(0))
        cursor?.close()
    }

    @Test
    fun insertAndGetItem() {

        // BEFORE : Adding demo item
        val propertyUri = mContentResolver?.insert(ItemContentProvider().URI_ESTATE, generateEstate())

        // TEST
        val cursor = mContentResolver?.query(ContentUris.withAppendedId(ItemContentProvider().URI_ESTATE, USER_ID), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor?.count, `is`(1))
        assertThat(cursor?.moveToFirst(), `is`(true))
        assertThat(cursor?.getString(cursor.getColumnIndexOrThrow("description")), `is`("Une petite description pour un test"))
    }

    private fun generateEstate(): ContentValues {

        val values = ContentValues()
        values.put("type", "Flat")
        values.put("address", "4 rue des soupirs")
        values.put("price", 150000)
        values.put("surface", 120)
        values.put("roomsCount", 5)
        values.put("bathroomsCount", 2)
        values.put("bedroomsCount", 1)
        values.put("description", "Une petite description pour un test")
        values.put("PointOfInterest", "parc, hotel, cinema")
        values.put("pictureList", "https://v.seloger.com/s/crop/310x225/visuels/0/m/l/4/0ml42xbt1n3itaboek3qec5dtskdgw6nlscu7j69k.jpg")
        values.put("descriptionPictureList", "home")
        values.put("status", "Available")
        values.put("entryDate", Date().time)
        values.put("saleDate", Date().time)
        values.put("agent", "Vincent")
        return values
    }

    companion object {
        private const val USER_ID: Long = 1
    }
}