package com.example.restaurantpos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.db.entity.TableEntity

@Dao
interface CustomerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCustomer(data : CustomerEntity) : Long

    @Delete
    fun deleteCustomer(data: CustomerEntity): Int

    @Query("SELECT * FROM customer WHERE customer_id = :id")
    fun getCustomer(id : Int) : LiveData<CustomerEntity>

    @Query("SELECT * FROM customer")
    fun getListCustomer() : LiveData<MutableList<CustomerEntity>>

    // Phục vụ cho việc tìm kiếm Khách
    @Query("SELECT * FROM customer WHERE phone LIKE :phone")
    fun getListCustomerByPhoneForSearch(phone : String) : LiveData<MutableList<CustomerEntity>>
    @Query("SELECT * FROM customer WHERE phone = :phone ")
    fun getListCustomerByPhoneForAdd(phone : String) : LiveData<MutableList<CustomerEntity>>



}