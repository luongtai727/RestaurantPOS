package com.example.restaurantpos.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountRoleEntity
import com.example.restaurantpos.db.entity.AccountShiftEntity
import com.example.restaurantpos.db.entity.AccountStatusEntity
import com.example.restaurantpos.db.entity.CartItemStatusEntity
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.db.entity.CouponEntity
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.db.entity.OrderStatusEntity
import com.example.restaurantpos.db.entity.ShiftEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.db.entity.TableStatusEntity
import com.example.restaurantpos.db.roomdb.PosRoomDatabase
import com.example.restaurantpos.ui.manager.category.CategoryViewModel
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    fun addAccount(accountEntity: AccountEntity){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.accountDAO.addAccount(accountEntity)
        }
    }

    fun addListAccount(listAccount: List<AccountEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.accountDAO.addListAccount(listAccount)
        }
    }

    fun addListAccountRole(listAccountRole: List<AccountRoleEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.accountDAO.addListAccountRole(listAccountRole)
        }
    }

    fun addListAccountStatus(listAccountStatus: List<AccountStatusEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.accountDAO.addListAccountStatus(listAccountStatus)
        }
    }

    fun addUser(context: Context, user: AccountEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            PosRoomDatabase.getInstance(context).accountDAO().addAccount(user)
        }
    }



    fun addListShift(listShift: List<ShiftEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.shiftDAO.addListShift(listShift)
        }
    }

    fun addCategory(data: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addCategory(data)
        }
    }

    fun addCategoryItem(categoryItem: ItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addCategoryItem(categoryItem)
        }
    }

    fun addListCategoryItem(listCategoryItem: List<ItemEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addListCategoryItem(listCategoryItem)
        }
    }

    fun addListCartItemStatus(listCartItemStatus: List<CartItemStatusEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.cartItemDAO.addListCartItemStatus(listCartItemStatus)
        }
    }

    fun addTable(context: Context, table: TableEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            PosRoomDatabase.getInstance(context).tableDAO().addTable(table)
        }
    }

    fun addListTableStatus(listTableStatus: List<TableStatusEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.tableDAO.addListTableStatus(listTableStatus)
        }
    }

    fun addCustomer(data: CustomerEntity) {
        CoroutineScope(Dispatchers.IO).launch{
            DatabaseUtil.addCustomer(data)
        }
    }

    fun addAccountShift(accountShift: AccountShiftEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.shiftDAO.addAccountShift(accountShift)
        }
    }

    fun addListOrderStatus(listOrderStatus: List<OrderStatusEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.orderDAO.addListOrderStatus(listOrderStatus)
        }
    }

    fun addCoupon(coupon: CouponEntity) {
        CoroutineScope(Dispatchers.IO).launch {
                DatabaseUtil.couponDAO.addCoupon(coupon)
        }
    }

}