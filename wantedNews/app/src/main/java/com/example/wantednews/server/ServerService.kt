package com.example.wantednews.server

import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object ServerService {
    private var instance: Retrofit? = null

    fun getInstance() = if (instance == null) {
        instance = Retrofit.Builder()
            .baseUrl(Constants.ServerURI.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        instance?.create(ServerApi::class.java)
    } else {
        instance?.create(ServerApi::class.java)
    }

//    fun getTopHeadlines(serverCallback: ServerCallback, country: String?, category: String?, search: String?, pageSize: Int?, page: Int?) {
//        val config = getInstance()?.create(ServerApi::class.java)
//        config?.getHeadLines(Constants.ServerURI.API_KEY, country, category, search, pageSize ?: 20, page )?.enqueue(object: Callback<TopHeadlinesData.TopHeadlines> {
//            override fun onResponse(call: Call<TopHeadlinesData.TopHeadlines>, response: Response<TopHeadlinesData.TopHeadlines>) {
//                if (response.isSuccessful) {
//                    serverCallback.onSuccessResponse(call, response)
//                } else {
//                    serverCallback.onFailResponse(call, response.code(), response.errorBody(), null)
//                }
//            }
//
//            override fun onFailure(call: Call<TopHeadlinesData.TopHeadlines>, t: Throwable) {
//                serverCallback.onFailResponse(call, 0, null, t)
//            }
//        })
//    }
}