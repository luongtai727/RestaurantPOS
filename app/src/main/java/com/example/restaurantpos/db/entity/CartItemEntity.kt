package com.example.restaurantpos.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cart_item")
data class CartItemEntity constructor(

    // Sử dụng order_create_time
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cart_item_id")
    val cart_item_id: Int,

    @ColumnInfo(name = "item_id")
    val item_id: Int,

    // Vì Order của mình String.
    @ColumnInfo(name = "order_id")
    val order_id: String,
    
    @ColumnInfo(name = "order_quantity")
    var order_quantity: Int,

    @ColumnInfo(name = "note")
    var note: String,

    @ColumnInfo(name = "cart_order_time")
    var cart_order_time: String,

    @ColumnInfo(name = "cart_item_status_id")
    var cart_item_status_id: Int

    /* Status.
0: Waiting
1: In Process
2: Done
3: Served
 */

): Parcelable {
    companion object {
        fun toCartItemObject(json: String): CartItemEntity? {
            return Gson().fromJson(json, CartItemEntity::class.java)
        }
    }



    fun toJson(): String {
        return Gson().toJson(this)
    }

}