package com.openclassrooms.realestatemanager.models

import androidx.room.TypeConverter
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String): Any? {
        val listType = object : TypeToken<ArrayList<String>>() {

        }.type
        return Gson().fromJson<Any>(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
