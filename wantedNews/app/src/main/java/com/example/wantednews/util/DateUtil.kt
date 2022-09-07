package com.example.wantednews.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun compareDate(newsDate: String): String {
        var result = ""
        try {
            val newsCalendar = Calendar.getInstance().apply {
                time = usTimeToKRTime(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(newsDate)!!)
            }
            val nowCalendar = Calendar.getInstance().apply {
                time = Date(System.currentTimeMillis())
            }

            val compareTime = nowCalendar.timeInMillis - newsCalendar.timeInMillis
            val diffYear = nowCalendar.get(Calendar.YEAR) - newsCalendar.get(Calendar.YEAR)
            val diffMonth = nowCalendar.get(Calendar.MONTH) - newsCalendar.get(Calendar.MONTH)
            val diffDay = nowCalendar.get(Calendar.DATE) - newsCalendar.get(Calendar.DATE)
            val diffHour = compareTime / (1000 * 3600)
            val diffMin = compareTime / (1000 * 60)

            result = when {
                diffYear > 0 -> {
                    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(newsCalendar.time)
                }
                diffMonth > 0 -> {
                    SimpleDateFormat("MM-dd hh:mm", Locale.getDefault()).format(newsCalendar.time)
                }
                diffDay > 0 -> {
                    if (diffDay == 1) {
                        "yesterday"
                    } else {
                        SimpleDateFormat("d 'days ago'", Locale.getDefault()).format(compareTime)
                    }
                }
                diffHour > 0 -> {
                    if (diffHour == 1L) {
                        "an hour ago"
                    } else {
                        SimpleDateFormat("H 'hours ago'", Locale.getDefault()).format(compareTime)
                    }
                }
                else -> {
                    if (diffMin == 0L) {
                        "now"
                    } else if (diffMin == 1L) {
                        "1 minute ago"
                    } else {
                        SimpleDateFormat("m 'minutes ago'", Locale.getDefault()).format(compareTime)
                    }
                }
            }
        } catch (e: Exception) {
            result = newsDate
            e.printStackTrace()
        }
        return result
    }

    private fun usTimeToKRTime(date: Date): Date {
        val offset = SimpleDateFormat("HH", Locale.getDefault()).parse("18")!!
        return Date(date.time + offset.time)
    }
}