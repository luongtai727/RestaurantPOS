package com.example.restaurantpos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.CouponEntity

@Dao
interface CouponDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCoupon(coupon: CouponEntity): Long

    @Delete
    fun deleteCoupon(coupon: CouponEntity): Int

    @Query("SELECT * from coupon WHERE coupon_status < 2 ORDER BY coupon_status DESC")
    fun getAllCoupon(): LiveData<MutableList<CouponEntity>>

    @Query("SELECT * from coupon WHERE coupon_status != 0")
    fun getAllCouponActive(): LiveData<MutableList<CouponEntity>>

    @Query("SELECT * from coupon WHERE coupon_code = :couponCode AND coupon_status != 0")
    fun getCouponActiveByCouponCode(couponCode: String): CouponEntity

    @Query("SELECT * from coupon WHERE coupon_code = :couponCode")
    fun getCouponByCouponCode(couponCode: String): MutableList<CouponEntity>


    /*
        @Query("SELECT * from account WHERE account_id = :account_id")
        fun getCouponById(account_id: Int): LiveData<MutableList<AccountEntity>>

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
        fun getStaffByName(staffName: String): LiveData<MutableList<AccountEntity>>*/


}