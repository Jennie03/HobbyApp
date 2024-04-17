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

class DetailViewModel(application:Application):AndroidViewModel(application) {
    val newsLD = MutableLiveData<News>()
    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun fetch(id: Int){
        Log.d("volley", "mskvolley")

        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/news/news.json"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                Log.d("showvolley", it)
                val sType = object : TypeToken<List<News>>(){}.type
                val result = Gson().fromJson<List<News>>(it, sType)
                val newsList = result as ArrayList<News>
                newsLD.value = newsList[id - 1]

                Log.d("showvolley", result.toString())
            },
            {
                Log.d("showvolley", it.toString())
            }
        )

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}