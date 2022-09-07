package com.example.wantednews.ui.activity.main.fragment.categories

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.wantednews.R
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.ErrorData
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.server.ServerService
import com.google.gson.Gson
import kotlinx.coroutines.*

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private var page = 1
    private val serverApi = ServerService.getInstance()
    private var coroutineJop: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onFailure(throwable.localizedMessage ?: "Error")
    }

    val isLoading = MutableLiveData<Boolean>()
    val categoryNewsList = MutableLiveData<ArrayList<TopHeadlinesData.Article>>().apply { value = arrayListOf() }

    val categoryList = ArrayList<Pair<String, Int>>().apply {
        add(Pair("business", R.drawable.ic_business))
        add(Pair("entertainment", R.drawable.ic_entertainment))
        add(Pair("general", R.drawable.ic_general))
        add(Pair("health", R.drawable.ic_health))
        add(Pair("science", R.drawable.ic_science))
        add(Pair("sports", R.drawable.ic_sports))
        add(Pair("technology", R.drawable.ic_technology))
    }

    fun requestNewsList(isMore: Boolean, category: String) {
        isLoading.value = true
        fetchNewsList(isMore, category)
    }

    private fun fetchNewsList(isMore: Boolean, category: String) {
        coroutineJop = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (isMore) {
                if (page * 20 == (categoryNewsList.value?.size ?: 0)) {
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
            val response = serverApi?.getHeadLines(Constants.ServerURI.API_KEY, country = Constants.Countries.COUNTRY_US, category = category, page = page)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    val responseData = response.body()
                    if (isMore) {
                        categoryNewsList.value = ArrayList<TopHeadlinesData.Article>().apply {
                            addAll(categoryNewsList.value ?: arrayListOf())
                            addAll(responseData?.articles ?: arrayListOf())
                        }
                    } else {
                        categoryNewsList.value = responseData?.articles ?: arrayListOf()
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