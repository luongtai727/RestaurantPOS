package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account_shift")
data class AccountShiftEntity constructor(

    @PrimaryKey(autoGenerate= true)
    @ColumnInfo(name = "account_shift_id")
    val account_shift_id: Int,

    @ColumnInfo(name = "shift_id")
    val shift_id: String,

    @ColumnInfo(name = "account_id")
    val account_id: Int

):Parcelable {
    companion object {
        fun toAccountShiftObject(json: String): AccountShiftEntity? {
            return Gson().fromJson(json, AccountShiftEntity::class.java)
        }
    }


    fun toJson(): String {
        return Gson().toJson(this)
    }

}