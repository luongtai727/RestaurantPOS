package com.example.restaurantpos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.db.entity.TableEntity

@Dao
interface ItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategoryItem(data: ItemEntity): Long

    @Delete
    fun deleteItemOfCategory(itemOfCategory: ItemEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListCategoryItem(listData: List<ItemEntity>): List<Long>

    // Lấy list Item--> Lấy Item cụ thể dựa vào index --> Lấy info của Item này.
    @Query("SELECT * from `item` WHERE category_id = :category_id")
    fun getListCategoryComponentItem(category_id: Int): LiveData<MutableList<ItemEntity>>

    @Query("SELECT * from `item` WHERE item_id = :item_id")
    fun getItemOfCategory(item_id: Int): LiveData<MutableList<ItemEntity>>

    @Query("SELECT * from `item` WHERE item_name = :name")
    fun getItemByName(name: String): List<ItemEntity>

    /* @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun addTable(table: TableEntity): Long

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun addListTable(listTable: List<TableEntity>): List<Long>

     @Delete
     fun deleteTable(table: TableEntity): Int

     @Query("SELECT * from `table`")
     fun getAllTable(): MutableList<TableEntity>

     @Query("SELECT * from `table` WHERE table_id = :id")
     fun getTableById(id: Int): TableEntity*/


}