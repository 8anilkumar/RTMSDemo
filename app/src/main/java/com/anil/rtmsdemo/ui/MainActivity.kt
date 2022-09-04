package com.anil.rtmsdemo.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.anil.rtmsdemo.adapter.TimeSlotAdapter
import com.anil.rtmsdemo.data.DatabaseHandler
import com.anil.rtmsdemo.databinding.ActivityMainBinding
import com.anil.rtmsdemo.models.MainResponse
import com.anil.rtmsdemo.models.ResponseData
import com.anil.rtmsdemo.models.TimeSlotModel
import com.anil.rtmsdemo.util.Constants.DB_NAME
import com.anil.rtmsdemo.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var responseData: ArrayList<ResponseData> = ArrayList()
    private var newResponseData: ArrayList<ResponseData> = ArrayList()
    private var timeSlotModelData: ArrayList<TimeSlotModel> = ArrayList()
    private var viewModel: MainViewModel? = null
    private lateinit var databaseHandler: DatabaseHandler
    private var morningData: ArrayList<ResponseData> = ArrayList()
    private var afterNoonData: ArrayList<ResponseData> = ArrayList()
    private var eveningData: ArrayList<ResponseData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpDB()
        setViewModels()

        /*
        ** Check internet connection
         */
        if (hasInternetConnection()){
            getTimeSlotList()
        } else {
            fetchRemoteData()
        }
    }

    private fun setViewModels() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun getTimeSlotList() {
        lifecycleScope.launch {
            responseData.clear()
            binding.mainProgressbar.visibility = View.VISIBLE

            /*
            ** Call API for fetching data from server
             */
            viewModel?.callApiForResponseData()

            /*
            ** Fetch data from view model
             */
            viewModel?.getResponseData()?.observe(this@MainActivity) { response: MainResponse? ->
                if (response != null) {
                    binding.mainProgressbar.visibility = View.GONE
                    response.let {
                        responseData.addAll(it.response)

                        /*
                        ** fetching remote data from db
                         */
                        val remoteData = databaseHandler.timeSlotInterface()?.readRemoteData()

                        /*
                        ** Delete remote data first
                         */
                        remoteData?.let {
                            databaseHandler.timeSlotInterface()?.deleteRemoteData(it)
                        }

                        /*
                        ** Save data in db
                         */
                        offlineCacheData(responseData)

                        /*
                        ** Fetch remote data from db
                         */
                        fetchRemoteData()

                    }
                } else {
                    /*
                    ** Fetch remote data from db
                    */
                    fetchRemoteData()
                    binding.mainProgressbar.visibility = View.GONE
                }
            }
        }
    }

    private fun offlineCacheData(responseData: ArrayList<ResponseData>) {
        val dataEntity = MainResponse(response = responseData)
        databaseHandler.timeSlotInterface()?.insertAlbum(dataEntity)
    }

    /*
    ** Fetch remote data from db
    */
    private fun fetchRemoteData() {
        val remoteData = databaseHandler.timeSlotInterface()?.readRemoteData()
        if (remoteData != null){
            remoteData.let {
                newResponseData.addAll(it.response)
                validateTime(newResponseData)
            }
        } else {
            Toast.makeText(binding.root.context,"Please check your internet connection",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setUpDB() {
        databaseHandler = Room.databaseBuilder(binding.root.context, DatabaseHandler::class.java, DB_NAME).allowMainThreadQueries().build()
    }

    fun validateTime(updatedList: ArrayList<ResponseData>) {
        morningData.clear()
        afterNoonData.clear()
        eveningData.clear()

        for (item in updatedList) {
            val time = item.slot_start_time.take(2)
            val timeInSec = time.toInt()
            when (timeInSec) {
                in 7..11 -> {
                    morningData.add(item)
                }
                in 11..14 -> {
                    afterNoonData.add(item)
                }
                in 14..17 -> {
                    eveningData.add(item)
                }
            }
        }

        timeSlotModelData.add(TimeSlotModel("Monrnig", morningData))
        timeSlotModelData.add(TimeSlotModel("Afternoon", afterNoonData))
        timeSlotModelData.add(TimeSlotModel("Evening", eveningData))
        timeSlotModelData.add(TimeSlotModel("All Slot", updatedList))

        bindList(timeSlotModelData)

    }

    private fun bindList(responseData: List<TimeSlotModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        val adapter = TimeSlotAdapter(binding.root.context, responseData)
        binding.recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

