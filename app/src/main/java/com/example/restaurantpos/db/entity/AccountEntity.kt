package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account")
data class AccountEntity constructor(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    val account_id: Int,

    @ColumnInfo(name = "account_name")
    var account_name: String,

    @ColumnInfo(name = "account_birthday")
    var account_birthday: String,

    @ColumnInfo(name = "account_phone")
    var account_phone: String,

    @ColumnInfo(name = "user_name")
    var user_name: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "role_id")
    val role_id: Int,

    @ColumnInfo(name = "account_status_id")
    var account_status_id: Boolean


    /*
    0: Manager
    1: Receptionist
    2: Kitchen
     */

) : Parcelable {
    companion object {
        fun toAccount(json: String): AccountEntity? {
            return Gson().fromJson(json, AccountEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}