package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account_role")
data class AccountRoleEntity constructor(

    @PrimaryKey
    @ColumnInfo(name = "account_role_id")
    val account_role_id: Int,

    @ColumnInfo(name = "description")
    val description: String


) : Parcelable {
    companion object {
        fun toAccountRole(json: String): AccountRoleEntity? {
            return Gson().fromJson(json, AccountRoleEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}