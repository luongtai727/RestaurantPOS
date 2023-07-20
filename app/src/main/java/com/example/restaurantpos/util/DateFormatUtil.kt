package com.example.restaurantpos.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatUtil {
    const val formatDate = "yyyy/MM/dd"
    const val formatDateAndMore = "yyyy/MM/dd  HH:mm:ss"
    const val formatNotContainDay = "HH:mm:ss"

    @SuppressLint("SimpleDateFormat")
    fun convertStringToDate(data: String): Date? {
        val dateFormat = SimpleDateFormat(formatDate)
        return dateFormat.parse(data)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToString(data: Date): String {
        val dateFormat = SimpleDateFormat(formatNotContainDay)
        return dateFormat.format(data)
    }

    fun getTimeForOrderCreateTime(): String {
        val simpleDateFormat = SimpleDateFormat(formatDateAndMore, Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    fun getTimeForKitchen(): String {
        val simpleDateFormat = SimpleDateFormat(formatNotContainDay, Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    fun getDateForCoupon(): String {
        val simpleDateFormat = SimpleDateFormat(formatDate, Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    fun getShiftId(year: Int, month: Int): String {
        return "${year}/${month}"
    }

    fun getShiftId(year: Int, month: Int, day: Int, shift: Int): String {
        return "${year}/${month}/${day}  $shift"
    }


}


