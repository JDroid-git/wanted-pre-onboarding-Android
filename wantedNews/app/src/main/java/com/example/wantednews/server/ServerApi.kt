package com.example.wantednews.server

import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ServerApi {
    @GET(Constants.ServerURI.TOP_HEADLINES)
    suspend fun getHeadLines(
        @Header("X-Api-Key") apiKey: String,
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("q") search: String? = null,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int? = null
    ): Response<TopHeadlinesData.TopHeadlines>
}