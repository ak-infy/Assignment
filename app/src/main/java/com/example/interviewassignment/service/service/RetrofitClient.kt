package com.example.interviewassignment.service.service

import com.example.interviewassignment.utils.AppConfig
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory


/*
Client class to handle the retrofit functions
 */
object RetrofitClient {
    private var retrofit: Retrofit? = null
    val retrofitInstance: Retrofit
        get() = retrofit?:Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}