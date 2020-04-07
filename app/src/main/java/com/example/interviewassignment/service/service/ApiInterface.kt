package com.example.interviewassignment.service.service

import com.example.interviewassignment.service.model.ArtifactModel
import com.example.interviewassignment.service.model.ListModel
import com.example.interviewassignment.utils.AppConfig
import retrofit2.Call
import retrofit2.http.GET

/*
Service interface for Facts entities
*/
interface ApiInterface {
    // Retrieves the facts feed
    @get:GET(AppConfig.ARTIFACT)
    val getArtifact: Call<ArtifactModel?>
    //@TODO: Check for the "Call? Will it nullable?
}
