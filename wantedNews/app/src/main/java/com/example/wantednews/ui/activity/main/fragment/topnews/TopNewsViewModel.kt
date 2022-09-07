package com.example.wantednews.ui.activity.main.fragment.topnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.server.ServerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopNewsViewModel : ViewModel() {

    private var page = 1
    private val serverApi = ServerService.getInstance()

    val isLoading = MutableLiveData<Boolean>()
    val newsList = MutableLiveData<ArrayList<TopHeadlinesData.Article>>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun requestNewsList(isMore: Boolean) {
        isLoading.value = true
        fetchNewsList(isMore)
    }

    private fun fetchNewsList(isMore: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isMore) {
                page.inc()
            }
            val response = serverApi?.getHeadLines(Constants.ServerURI.API_KEY, Constants.Countries.COUNTRY_KR, page = page)
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

                    isLoading.value = false
                }
            }
        }
    }
}