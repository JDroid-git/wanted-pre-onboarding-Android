package com.example.wantednews.server

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

interface ServerCallback {
    fun <Any> onSuccessResponse(call: Call<Any>, response: Response<Any>)
    fun <Any> onFailResponse(call: Call<Any>, responseCode: Int?, response: ResponseBody?, t: Throwable? )
}