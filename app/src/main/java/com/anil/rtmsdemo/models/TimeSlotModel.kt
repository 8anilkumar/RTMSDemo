package com.anil.rtmsdemo.models

data class TimeSlotModel(
    val slatTime: String,
    val timeSlotList :ArrayList<ResponseData>,
    var isExpandable: Boolean = false
)
