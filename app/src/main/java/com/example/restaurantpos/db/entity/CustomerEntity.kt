package com.example.restaurantpos.db.entity


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "customer")
data class CustomerEntity constructor(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_id")
    val customer_id: Int,

    @ColumnInfo(name = "customer_name")
    val customer_name: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "birthday")
    val birthday: String,

    @ColumnInfo(name = "total_payment")
    val total_payment: Double


    /*  Thêm để bổ trợ gì mới thêm chứ không thì thôi. Ví dụ khi tạo Customer có cần below không?--> NO
        @ColumnInfo(name = "created_by_account_id")
        val created_by_account_id: Int*/

    /*    @ForeignKey (
            entity = AccountEntity::class,
            parentColumns = ["account_id"],
            childColumns = ["created_by_account_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
            deferred = false
        )
        @ColumnInfo(name = "created_by_account_id")
        val created_by_account_id: String*/

) : Parcelable {
    companion object {
        fun toCustomerEntity(jsonData: String): CustomerEntity? {
            return Gson().fromJson(jsonData, CustomerEntity::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}