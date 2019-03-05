package com.openclassrooms.realestatemanager.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter {
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