package com.anil.rtmsdemo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.anil.rtmsdemo.models.MainResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeSlotInterface {

    @Insert
    fun insertAlbum(addAlbum: MainResponse)

    @Query("SELECT * FROM rtms_table")
    fun readRemoteData(): MainResponse

    @Delete
     fun deleteRemoteData(mainResponse: MainResponse)

}