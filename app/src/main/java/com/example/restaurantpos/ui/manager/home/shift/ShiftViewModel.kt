package com.example.restaurantpos.ui.manager.home.shift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.AccountShiftEntity
import com.example.restaurantpos.db.entity.ShiftEntity
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShiftViewModel : ViewModel() {

    private val _isDuplicate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isDuplicate: LiveData<Boolean> = _isDuplicate

    fun addAccountShift(accountShift: AccountShiftEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingAccountShift = DatabaseUtil.shiftDAO.getShiftByNameAndShift(accountShift.shift_id, accountShift.account_id)

            if (existingAccountShift == null) {
                // Nếu tài khoản chưa tồn tại, thêm vào cơ sở dữ liệu
                DatabaseUtil.shiftDAO.addAccountShift(accountShift)
                _isDuplicate.postValue(false)
            } else {
                // Nếu tài khoản đã tồn tại, xử lý tương ứng (ví dụ: hiển thị thông báo lỗi)
                _isDuplicate.postValue(true)
            }
        }
    }


    fun addShift(shift: ShiftEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.shiftDAO.addShift(shift)
        }
    }

    fun getShiftById(shift_id: String) = DatabaseUtil.getShiftById(shift_id)

    fun getListShift() = DatabaseUtil.getListShift()

/*    fun addAccountShift(accountShift: AccountShiftEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.shiftDAO.addAccountShift(accountShift)
        }
    }*/



    fun deleteAccountShift(accountShift: AccountShiftEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.shiftDAO.deleteAccountShift(accountShift)
        }
    }


    fun getListAccountShift(shift_id: String) = DatabaseUtil.getListAccountShift(shift_id)
    fun getListAccountShiftReceptionist(shift_id: String) = DatabaseUtil.getListAccountShiftReceptionist(shift_id)
    fun getListAccountShiftKitchen(shift_id: String) = DatabaseUtil.getListAccountShiftKitchen(shift_id)


    fun getListAccountShiftForSetListData(shift_id: String) = DatabaseUtil.getListAccountShiftForSetListData(shift_id)


}