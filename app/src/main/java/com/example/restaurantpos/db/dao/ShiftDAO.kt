package com.example.restaurantpos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantpos.db.entity.AccountShiftEntity
import com.example.restaurantpos.db.entity.ShiftEntity

@Dao
interface ShiftDAO {

    /** ShiftEntity */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShift(shift: ShiftEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListShift(listShift: List<ShiftEntity>): List<Long>

    @Query("SELECT * from shift WHERE shift_id = :shift_id")
    fun getShiftById(shift_id: String): LiveData<ShiftEntity>


    // Qnew
    @Query("SELECT * from account_shift WHERE shift_id = :shift_id AND account_id = :account_id")
    fun getShiftByNameAndShift(shift_id: String, account_id: Int): AccountShiftEntity

    @Query("SELECT * from shift")
    fun getListShift(): LiveData<MutableList<ShiftEntity>>

    /** AccountShiftEntity */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccountShift(accountShift: AccountShiftEntity): Long

    @Delete
    fun deleteAccountShift(accountShift: AccountShiftEntity): Int

    // shift_id dạng:    yyyy/MM/dd_shift_name
    // Lấy ra toàn bộ account hoạt động trong Shift --> Đưa vào ID của Shift

    @Query("SELECT * from account_shift WHERE shift_id = :shift_id")
    fun getListAccountShiftForSetListData(shift_id: String): LiveData<MutableList<AccountShiftEntity>>

    @Query("SELECT account.account_name from account_shift JOIN account ON account_shift.account_id = account.account_id WHERE shift_id LIKE :shift_id")
    fun getListAccountShift(shift_id: String): LiveData<MutableList<String>>

    // Filter riêng dành cho Staff
    // Dùng cho ShiftOfStaffFragment --> Lại cần argument truyền sang để đánh dấu nó là nó từ thằng nào sang
    // Lúc này có thể tái sử dụng 1 màn thôi
    @Query("SELECT account.account_name from account_shift JOIN account ON account_shift.account_id = account.account_id WHERE shift_id LIKE :shift_id AND role_id = 1")
    fun getListAccountShiftReceptionist(shift_id: String): LiveData<MutableList<String>>

    @Query("SELECT account.account_name from account_shift JOIN account ON account_shift.account_id = account.account_id WHERE shift_id LIKE :shift_id AND role_id = 2")
    fun getListAccountShiftKitchen(shift_id: String): LiveData<MutableList<String>>

}