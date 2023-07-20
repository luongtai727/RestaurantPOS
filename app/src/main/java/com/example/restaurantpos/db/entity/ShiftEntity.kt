package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "shift")
data class ShiftEntity constructor(

    @PrimaryKey
    @ColumnInfo(name = "shift_id")
    val shift_id: String,

    @ColumnInfo(name = "shift_name")
    val shift_name: String,

    @ColumnInfo(name = "start_time")
    val start_time: String,

    @ColumnInfo(name = "end_time")
    val end_time: String

//    Chốt: shift_id dạng:    yyyy/MM/dd_shift_name

    /*
    shift_name
    1. Morning
    2. Afternoon
    3. Night
     */

) : Parcelable {
    companion object {
        fun toShiftObject(json: String): ShiftEntity? {
            return Gson().fromJson(json, ShiftEntity::class.java)
        }
    }


    fun toJson(): String {
        return Gson().toJson(this)
    }

}