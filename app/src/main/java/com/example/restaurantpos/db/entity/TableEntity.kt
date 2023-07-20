package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "table")
data class TableEntity constructor(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "table_id")
    val table_id: Int,

    @ColumnInfo(name = "table_name")
    var table_name: String,

    @ColumnInfo(name = "table_capacity")
    val table_capacity: Int,

    @ColumnInfo(name = "table_status_id")
    var table_status_id: Int

    /*
    0. Empty     (Order mới)
    1. New Order (Người khác không chọn được)
    2. Old Order (Click vào là hiện ra Order thêm or thanh toán)
    3. Problem
     */

) : Parcelable {
    companion object {
        fun toTableEntity(json: String): TableEntity? {
            return Gson().fromJson(json, TableEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

}