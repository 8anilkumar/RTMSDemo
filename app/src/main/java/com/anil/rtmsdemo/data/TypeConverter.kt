package com.anil.rtmsdemo.data

import androidx.room.TypeConverter
import com.anil.rtmsdemo.models.ResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {

    private val gson = Gson()
    @TypeConverter
    fun jsonToString(image: List<ResponseData>): String {
        return gson.toJson(image)
    }

    @TypeConverter
    fun stringToJson(imageString: String): List<ResponseData> {
        val objectType = object : TypeToken<List<ResponseData>>() {}.type
        return gson.fromJson(imageString, objectType)
    }
}