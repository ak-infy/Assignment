package com.example.interviewassignment.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.example.interviewassignment.service.model.ListModel
import com.example.interviewassignment.service.service.DataRespository

class MainViewModel(application: Application): AndroidViewModel(application) {
    val data: LiveData<ListModel?>
        get() = DataRespository.getData()

    val offlineData: LiveData<ListModel?>?
        get() = DataRespository.getOfflineCachedData()
}