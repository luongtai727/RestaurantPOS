package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "table_status")
data class TableStatusEntity constructor(

    @PrimaryKey
    @ColumnInfo(name = "table_status_id")
    val table_status_id: Int,

    @ColumnInfo(name = "description")
    val description: String


) : Parcelable {
    companion object {
        fun toTableStatus(json: String): TableStatusEntity? {
            return Gson().fromJson(json, TableStatusEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}