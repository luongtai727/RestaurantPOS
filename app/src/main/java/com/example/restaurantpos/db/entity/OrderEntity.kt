package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

// Quản lý Các Order (Bill)
@Parcelize
@Entity(tableName = "order")
data class OrderEntity constructor(

    // Sử dụng order_create_time  (First Time)
    @PrimaryKey
    @ColumnInfo(name = "order_id")
    val order_id: String,

    @ColumnInfo(name = "customer_id")
    var customer_id: Int,

    @ColumnInfo(name = "table_id")
    val table_id: Int,

    @ColumnInfo(name = "created_by_account_id")
    val created_by_account_id: Int,

    // Latest Time
    @ColumnInfo(name = "order_create_time")
    var order_create_time: String,

    @ColumnInfo(name = "paid_time")
    var paid_time: String,

    @ColumnInfo(name = "bill_total")
    var bill_total: Float,

    @ColumnInfo(name = "order_status_id")
    var order_status_id: Int

    /*
    0. Đang trong quá trình tạo Bill (Mới click cái bàn xong)
    1. Chốt Bill nhưng chưa thanh toán
    2. Thanh toán rồi
    */

): Parcelable {
    companion object {
        fun toOrderObject(json: String): OrderEntity? {
            return Gson().fromJson(json, OrderEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

}