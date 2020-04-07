package com.example.interviewassignment.viewmodel

import androidx.databinding.BaseObservable
import com.example.interviewassignment.service.model.ListDataModel

class ListDataViewModel(private var listDataModel: ListDataModel): BaseObservable() {
    fun getListDataModel() = listDataModel
}