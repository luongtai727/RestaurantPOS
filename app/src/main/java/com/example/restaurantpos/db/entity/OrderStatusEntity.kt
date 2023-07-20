package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "order_status")
data class OrderStatusEntity constructor(

    @PrimaryKey
    @ColumnInfo(name = "order_status_id")
    val order_status_id: Int,

    @ColumnInfo(name = "description")
    val description: String


) : Parcelable {
    companion object {
        fun toOrderStatus(json: String): OrderStatusEntity? {
            return Gson().fromJson(json, OrderStatusEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}