package com.example.restaurantpos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.db.entity.TableEntity

@Dao
interface CategoryDAO {

    // Bản chất của LiveData là sống theo màn --> Màn tắt thì nó cũng tắt theo. Chẳng sợ call liên tục
    @Query("SELECT * from `category`")
    fun getAllCategory(): LiveData<MutableList<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(data: CategoryEntity): Long


}