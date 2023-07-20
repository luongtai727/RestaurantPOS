package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account_status")
data class AccountStatusEntity constructor(

    @PrimaryKey
    @ColumnInfo(name = "account_status_id")
    val account_status_id: Boolean,

    @ColumnInfo(name = "description")
    val description: String


) : Parcelable {
    companion object {
        fun toAccountStatus(json: String): AccountStatusEntity? {
            return Gson().fromJson(json, AccountStatusEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}