package com.ubaya.hobbyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.hobbyapp.model.News

class ListViewModel(application: Application): AndroidViewModel(application) {
    val newsLD = MutableLiveData<ArrayList<News>>()
    val newsLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun refresh(){
        loadingLD.value = true
        newsLoadErrorLD.value = true

        Log.d("mskvolley", "masuk")

        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/news/news.json"

        val stringRequest = StringRequest(Request.Method.GET, url,
            {
                val sType = object : TypeToken<List<News>>(){ }.type
                val result = Gson().fromJson<List<News>>(it, sType)
                newsLD.value = result as ArrayList<News>?
                loadingLD.value = false

                Log.d("showvolley", result.toString())
                Log.d("showvolley", it)

            },
            {
                Log.d("showvolley", it.toString())
                newsLoadErrorLD.value = false
                loadingLD.value = false
            })

        stringRequest.tag = TAG
        queue?.add(stringRequest)

        loadingLD.value = false
        newsLoadErrorLD.value = false
    }
    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}