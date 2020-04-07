package com.example.interviewassignment.service.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.interviewassignment.service.model.ArtifactDataModel
import com.example.interviewassignment.service.model.ArtifactModel
import com.example.interviewassignment.service.model.ListDataModel
import com.example.interviewassignment.service.model.ListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataRespository {
    private lateinit var listModelMutableLiveData: MutableLiveData<ListModel>

    private fun getCachedData(): LiveData<ListModel?> {
        return listModelMutableLiveData

    }

    fun getData(): LiveData<ListModel?> {
        val service = RetrofitClient.retrofitInstance.create(ApiInterface::class.java)
        service.getArtifact.enqueue(object : Callback<ArtifactModel?> {
            init {
                listModelMutableLiveData = MutableLiveData()
            }
            override fun onResponse(call: Call<ArtifactModel?>, response: Response<ArtifactModel?>) {
                Log.d("TESTING1", "response : " + response)
                response.body()
                    ?. let{
                        Log.d("TESTING2", "ArtifactModel : " + it)
                        val listModel = removeNullDataFromResponse(it)
                        Log.d("TESTING3", "listModel : " + listModel)
                        listModelMutableLiveData.value = listModel
                    }
                    ?: listModelMutableLiveData.setValue(null)
            }

            override fun onFailure(call: Call<ArtifactModel?>, t: Throwable) {
                listModelMutableLiveData.setValue(null)
            }
        })

        return listModelMutableLiveData
    }

    fun getOfflineCachedData(): LiveData<ListModel?>? = if ((::listModelMutableLiveData::isInitialized).get()) {
        listModelMutableLiveData
    } else {
        null
    }

    /**
     * Removes null data to avoid blank row in recyclerView
     */
    private fun removeNullDataFromResponse(artifactModel: ArtifactModel): ListModel {
        val rows = artifactModel.rows?:ArrayList<ArtifactDataModel>(0)
        val list = ArrayList<ListDataModel>(rows.size)
        for (i in 0 until rows.size) {
            val data = rows[i]
            Log.d("TESTING", "data : " + data)
            data
                ?.let {
                    list.add(ListDataModel(
                        it.title
                            ?.let {
                                if (it.isNotBlank())
                                    it
                                else
                                    "No Title Available"
                            }
                            ?:"NULL Title Sent",
                        it.description
                            ?.let {
                                if (it.isNotBlank())
                                    it
                                else
                                    "No Description Available"
                            }
                            ?:"NULL Description Sent",
                        it.imageHref))
                }
                ?: continue
        }
        return ListModel(artifactModel.title?:"No List Title Available", list)
    }


}