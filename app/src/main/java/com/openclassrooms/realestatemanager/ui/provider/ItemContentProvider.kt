package com.openclassrooms.realestatemanager.ui.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.fromContentValues

class ItemContentProvider : ContentProvider() {

    private val AUTHORITY = "com.openclassrooms.realestatemanager.ui.provider"
    private val TABLE_NAME = Property::class.java.simpleName
    var URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
        if (context != null && p1 != null) {
            Log.e("EstateContentProvider", "ContentValues : $p1")
            val index = RealEstateDatabase.realEstateDatabase.propertyDao().insertNewProperty(fromContentValues(p1))
            if (index != 0L) {
                context.contentResolver.notifyChange(p0, null)
                return ContentUris.withAppendedId(p0, index)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $p0")
    }

    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor {
        if (context != null) {
            val index: Long = ContentUris.parseId(p0)
            val cursor = RealEstateDatabase.realEstateDatabase.propertyDao().getItemsWithCursor(index)
            cursor.setNotificationUri(context.contentResolver, p0)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $p0")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        if (context != null && p1 != null) {
            val count: Int = RealEstateDatabase.realEstateDatabase.propertyDao().updateProperty(fromContentValues(p1))
            context.contentResolver.notifyChange(p0, null)
            return count
        }

        throw IllegalArgumentException("Failed to update row into $p0")
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {
        throw IllegalArgumentException("You can't delete anything")
    }

    override fun getType(p0: Uri?): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }
}