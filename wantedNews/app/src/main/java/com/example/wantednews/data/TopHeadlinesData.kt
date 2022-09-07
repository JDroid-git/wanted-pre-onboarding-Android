package com.example.wantednews.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object TopHeadlinesData {
    @Parcelize
    data class TopHeadlines(
        val status: String?,
        val totalResult: Int?,
        val articles: ArrayList<Article>?
    ) : Parcelable

    @Parcelize
    data class Source(
        val name: String?,
        val id: String?
    ) : Parcelable

    @Parcelize
    data class Article(
        val source: Source?,
        val author: String?,
        val title: String?,
        val description: String?,
        val url: String?,
        val urlToImage: String?,
        val publishedAt: String?,
        val content: String?,
    ) : Parcelable
}

