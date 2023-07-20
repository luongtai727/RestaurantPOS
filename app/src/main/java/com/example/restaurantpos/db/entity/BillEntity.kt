package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class BillEntity constructor(

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "bill_id")
//    val bill_id: Int,

//    @ColumnInfo(name = "bill_date")
    var bill_date: String,

//    @ColumnInfo(name = "bill_table_name")
    var bill_table_name: String,

//    @ColumnInfo(name = "bill_customer")
    var bill_customer: String,

//    @ColumnInfo(name = "bill_staff")
    var bill_staff: String,

//    @ColumnInfo(name = "bill_subTotal")
    var bill_subTotal: Float,

//    @ColumnInfo(name = "bill_coupon")
    val bill_coupon: Int,

//    @ColumnInfo(name = "bill_customer_rank_percent")
    var bill_customer_rank_percent: Int,

//    @ColumnInfo(name = "bill_tax")
    var bill_tax: Float,

//    @ColumnInfo(name = "bill_total")
    var bill_total: Float,

//    @ColumnInfo(name = "bill_cash")
    var bill_cash: String,

//    @ColumnInfo(name = "bill_change")
    var bill_change: String


) : Parcelable {
    companion object {
        fun toBill(json: String): BillEntity? {
            return Gson().fromJson(json, BillEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}