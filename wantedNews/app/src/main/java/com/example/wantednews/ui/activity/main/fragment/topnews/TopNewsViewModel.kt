package com.example.wantednews.ui.activity.main.fragment.topnews

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.ErrorData
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.server.ServerService
import com.google.gson.Gson
import kotlinx.coroutines.*

class TopNewsViewModel(application: Application) : AndroidViewModel(application) {

    private var page = 1
    private val serverApi = ServerService.getInstance()
    private var coroutineJop: Job? = null

    val isLoading = MutableLiveData<Boolean>()
    val newsList = MutableLiveData<ArrayList<TopHeadlinesData.Article>>().apply { value = arrayListOf() }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onFailure(throwable.localizedMessage ?: "Error")
    }

    fun requestNewsList(isMore: Boolean) {
        isLoading.value = true
        fetchNewsList(isMore)
    }

    private fun fetchNewsList(isMore: Boolean) {
        coroutineJop = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (isMore) {
                if (page * 20 == (newsList.value?.size ?: 0)) {
                    page++
                } else {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                    }
                    cancel()
                }
            } else {
                page = 1
            }
            val response = serverApi?.getHeadLines(Constants.ServerURI.API_KEY, country = Constants.Countries.COUNTRY_US, page = page)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    val responseData = response.body()
                    if (isMore) {
                        newsList.value = ArrayList<TopHeadlinesData.Article>().apply {
                            addAll(newsList.value ?: arrayListOf())
                            addAll(responseData?.articles ?: arrayListOf())
                        }
                    } else {
                        newsList.value = responseData?.articles ?: arrayListOf()
                    }
                    isLoading.value = false
                } else {
                    var errorMessage = ""
                    runCatching {
                        errorMessage = Gson().fromJson(response?.errorBody()?.string(), ErrorData::class.java)?.message ?: ""
                    }
                    onFailure(errorMessage)
                    isLoading.value = false
                }
            }
        }
    }

    private fun onFailure(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            isLoading.value = false
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        }
    }
}