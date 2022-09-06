package com.example.wantednews.ui.activity.main.fragment.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wantednews.R

class CategoryViewModel : ViewModel() {
    val categoryList = ArrayList<Pair<String, Int>>().apply{
        add(Pair("business", R.drawable.ic_business))
        add(Pair("entertainment", R.drawable.ic_entertainment))
        add(Pair("general", R.drawable.ic_general))
        add(Pair("health", R.drawable.ic_health))
        add(Pair("science", R.drawable.ic_science))
        add(Pair("sports", R.drawable.ic_sports))
        add(Pair("technology", R.drawable.ic_technology))
    }
}