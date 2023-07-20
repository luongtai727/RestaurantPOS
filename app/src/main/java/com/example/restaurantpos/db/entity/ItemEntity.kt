package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "item")
data class ItemEntity constructor(

    @PrimaryKey(autoGenerate= true)
    @ColumnInfo(name = "item_id")
    val item_id: Int,

    @ColumnInfo(name = "item_name")
    var item_name: String,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "inventory_quantity")
    var inventory_quantity: Int,

    @ColumnInfo(name = "category_id")
    val category_id: Int

): Parcelable {
    companion object {
        fun toItemObject(json: String): ItemEntity? {
            return Gson().fromJson(json, ItemEntity::class.java)
        }
    }



    fun toJson(): String {
        return Gson().toJson(this)
    }

}