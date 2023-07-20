package com.example.restaurantpos.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_rank")
data class CustomerRankEntity constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_rank_id")
    val customer_rank_id: Int,
    @ColumnInfo(name = "description")
    val description: String)