package com.anil.rtmsdemo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anil.rtmsdemo.models.MainResponse

@Database(entities = [MainResponse::class], exportSchema = false, version = 1)
@TypeConverters(TypeConverter::class)
abstract class DatabaseHandler : RoomDatabase() {
    abstract fun timeSlotInterface(): TimeSlotInterface?
}