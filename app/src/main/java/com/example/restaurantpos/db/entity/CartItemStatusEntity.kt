package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cart_item_status")
data class CartItemStatusEntity constructor(

    @PrimaryKey
    @ColumnInfo(name = "cart_item_status_id")
    val cart_item_status_id: Int,

    @ColumnInfo(name = "description")
    val description: String


) : Parcelable {
    companion object {
        fun toTableStatus(json: String): CartItemStatusEntity? {
            return Gson().fromJson(json, CartItemStatusEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}