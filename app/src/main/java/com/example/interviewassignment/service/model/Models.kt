package com.example.interviewassignment.service.model


// This allows to avoid many unecassary evlis and null checks and makes the code safe to write further
// The transformation happens in DataRepository
data class ArtifactModel (val title: String?, val rows: ArrayList<ArtifactDataModel?>?)
data class ArtifactDataModel (val title: String?, val description: String?, val imageHref: String?)
data class ListModel (val title: String, val rows: ArrayList<ListDataModel>)
data class ListDataModel (val title: String, val description: String, val imageRef: String?)

