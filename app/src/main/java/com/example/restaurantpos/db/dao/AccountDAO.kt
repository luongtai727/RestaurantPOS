package com.example.restaurantpos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountRoleEntity
import com.example.restaurantpos.db.entity.AccountStatusEntity

@Dao
interface AccountDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: AccountEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListAccount(listAccount: List<AccountEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListAccountRole(listAccountRole: List<AccountRoleEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListAccountStatus(listAccountStatus: List<AccountStatusEntity>): List<Long>

    @Delete
    fun deleteAccount(account: AccountEntity): Int

    @Query("SELECT * from account WHERE account_id = :account_id")
    fun getAccountById(account_id: Int): LiveData<MutableList<AccountEntity>>

    // Qnew
    @Query("SELECT * from account WHERE user_name = :username")
    fun getAccountByUsername(username: String): AccountEntity

    @Query("SELECT * from account WHERE user_name = :user_name AND password = :password")
    fun checkLogin(user_name: String, password: String): List<AccountEntity>

    @Query("SELECT * from account WHERE role_id != 0")
    fun getAllUser(): LiveData<MutableList<AccountEntity>>

    @Query("SELECT * from account")
    fun getAllAccount(): LiveData<MutableList<AccountEntity>>

    @Query("SELECT * from account WHERE role_id != 0 AND account_status_id = 1")
    fun getAllUserActive(): LiveData<MutableList<AccountEntity>>

    // Phục vụ cho việc add account_shift
    @Query("SELECT * from account WHERE role_id != 0 AND account_status_id = 1 AND account_name LIKE :name")
    fun getAllUserActiveByName(name: String): LiveData<MutableList<AccountEntity>>

    @Query("SELECT * from account WHERE account_name LIKE :staffName")
    fun getStaffByName(staffName: String): LiveData<MutableList<AccountEntity>>


}