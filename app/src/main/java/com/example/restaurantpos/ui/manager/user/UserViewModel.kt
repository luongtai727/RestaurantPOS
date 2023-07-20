package com.example.restaurantpos.ui.manager.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.roomdb.PosRoomDatabase
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    fun getAllUser() = DatabaseUtil.getAllUser()
    fun getAllUserActive() = DatabaseUtil.getAllUserActive()
    fun getAllUserActiveByName(name: String) = DatabaseUtil.getAllUserActiveByName(name)

    fun getAllUser(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            PosRoomDatabase.getInstance(context).accountDAO().getAllAccount()
        }
    }

    fun getStaffByName(staffName: String) =DatabaseUtil.getStaffByName(staffName)
    fun getAccountById(account_id: Int)  =DatabaseUtil.getAccountById(account_id)


/*    fun addUser(context: Context, user: AccountEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            PosRoomDatabase.getInstance(context).accountDAO().addAccount(user)
        }
    }*/

    private val _isDuplicate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isDuplicate: LiveData<Boolean> = _isDuplicate

    fun addUser(context: Context, user: AccountEntity) {
        CoroutineScope(Dispatchers.IO).launch {

            val existingAccountShift = PosRoomDatabase
                .getInstance(context)
                .accountDAO()
                .getAccountByUsername(user.user_name)

            if (existingAccountShift == null) {
                // Nếu tài khoản chưa tồn tại, thêm vào cơ sở dữ liệu
                PosRoomDatabase.getInstance(context).accountDAO().addAccount(user)
                _isDuplicate.postValue(false)
            } else {
                // Nếu tài khoản đã tồn tại, xử lý tương ứng (ví dụ: hiển thị thông báo lỗi)
                _isDuplicate.postValue(true)
            }
        }
    }

}