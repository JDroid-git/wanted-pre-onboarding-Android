package com.example.wantednews.server

import com.example.wantednews.constants.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
}