package com.anil.rtmsdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anil.rtmsdemo.models.MainResponse
import com.anil.rtmsdemo.network.Repository

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: Repository = Repository()
    lateinit var dataResponse: MutableLiveData<MainResponse?>

    /*
    ** Call network API
     */
    fun callApiForResponseData() {
        dataResponse = repository.getData()
    }

    /*
    ** Set Data in model
     */
    fun getResponseData(): MutableLiveData<MainResponse?> {
        return dataResponse
    }

}