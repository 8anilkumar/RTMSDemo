package com.anil.rtmsdemo.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anil.rtmsdemo.models.MainResponse
import com.anil.rtmsdemo.util.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    /*
    ** Fetch data from server
     */
    fun getData(): MutableLiveData<MainResponse?> {
        val responseData: MutableLiveData<MainResponse?> = MutableLiveData<MainResponse?>()
        ApiClient.instenceUser?.getResponseData?.enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, throwable: Throwable) {
                Log.e("Error", throwable.toString())
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.isSuccessful) {
                    val status = response.body()?.code
                    if (status == 200) {
                        response.body()?.let {
                            responseData.value = it
                         }
                      } else {
                        Log.v("Error", response.toString())
                    }
                } else {
                    Log.e("Error", response.toString())
                }
            }
        })

        return responseData
    }
}