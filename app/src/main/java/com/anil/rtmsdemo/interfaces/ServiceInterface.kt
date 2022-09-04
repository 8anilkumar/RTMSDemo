package com.anil.rtmsdemo.interfaces

import com.anil.rtmsdemo.models.MainResponse
import com.anil.rtmsdemo.util.Constants
import retrofit2.Call
import retrofit2.http.GET

interface ServiceInterface {

    /*
    ** Network call to server
     */
    @get:GET(Constants.ENDPOINT)
    val getResponseData: Call<MainResponse>

 }