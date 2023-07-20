//package com.example.restaurantpos.db.entity
//
//import android.os.Parcelable
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import com.google.gson.Gson
//import kotlinx.android.parcel.Parcelize
//
//@Parcelize
//@Entity(tableName = "token_table")
//data class TokenEntity @JvmOverloads constructor(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    var id: Int,
//    @ColumnInfo(name = "account_id")
//    var account_id: Int,
//    @ColumnInfo(name = "token")
//    var token: String,
//    @ColumnInfo(name = "date")
//    var date: String
///*
// date: Time hết hạn
// Ngay khi login thành công thì auto sẽ tạo thêm token
// */
//
//) : Parcelable {
//
//    companion object {
//        fun toTokenObject(jsonData: String): TokenEntity? {
//            return Gson().fromJson(jsonData, TokenEntity::class.java)
//        }
//    }
//
//    fun toJson(): String {
//        return Gson().toJson(this)
//    }
//}