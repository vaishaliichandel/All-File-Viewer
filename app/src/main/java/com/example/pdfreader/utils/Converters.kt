package com.example.pdfreader.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {

    @TypeConverter
    fun fromString(value: String?): Date {
        val listType = object : TypeToken<Date>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: Date): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}