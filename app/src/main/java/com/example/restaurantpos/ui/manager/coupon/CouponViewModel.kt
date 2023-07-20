package com.example.restaurantpos.ui.manager.coupon

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.restaurantpos.db.entity.CouponEntity
import com.example.restaurantpos.db.roomdb.PosRoomDatabase
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CouponViewModel : ViewModel() {

    private val _isDuplicate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isDuplicate: LiveData<Boolean> = _isDuplicate

    fun addCoupon(context: Context, couponQ: CouponEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingCoupon = PosRoomDatabase
                .getInstance(context)
                .couponDAO()
                .getCouponActiveByCouponCode(couponQ.coupon_code)

            if (existingCoupon == null) {
                DatabaseUtil.addCoupon(couponQ)
                _isDuplicate.postValue(false)
            } else {
                _isDuplicate.postValue(true)
            }
        }
    }

    fun addCouponUpdate(couponQ: CouponEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addCoupon(couponQ)
        }
    }

    fun getAllCoupon() = DatabaseUtil.getAllCoupon()

    fun getAllCouponActive() = DatabaseUtil.getAllCouponActive()

    private val _couponGetByCouponCode:  MutableLiveData<MutableList<CouponEntity>>  by lazy {
        MutableLiveData<MutableList<CouponEntity>>()
    }
    val couponGetByCouponCode: MutableLiveData<MutableList<CouponEntity>> = _couponGetByCouponCode
    fun getCouponByCouponCode(couponCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val couponGetByCouponCodeQ = DatabaseUtil.getCouponByCouponCode(couponCode)
            if (couponGetByCouponCodeQ != null){
                _couponGetByCouponCode.postValue(couponGetByCouponCodeQ)
            }
        }
    }





//    fun deleteCoupon(coupon: CouponEntity) = DatabaseUtil.couponDAO.deleteCoupon(coupon)


}