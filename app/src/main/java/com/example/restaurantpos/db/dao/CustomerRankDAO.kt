package com.example.restaurantpos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.restaurantpos.db.entity.CustomerRankEntity

@Dao
interface CustomerRankDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCustomerRank(data: CustomerRankEntity): Long
}