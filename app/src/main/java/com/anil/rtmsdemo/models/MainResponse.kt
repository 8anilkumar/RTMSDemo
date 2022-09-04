package com.anil.rtmsdemo.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anil.rtmsdemo.util.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MainResponse(
    val code: Int?=null,
    val error: Boolean? = null,
    val message: String ?= null,
    val response: List<ResponseData>) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}