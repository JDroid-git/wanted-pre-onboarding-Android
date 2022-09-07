package com.example.wantednews.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorData(
    val status: String,
    val code: String,
    val message: String
) : Parcelable