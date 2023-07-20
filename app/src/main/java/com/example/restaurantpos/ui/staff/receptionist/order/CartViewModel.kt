package com.example.restaurantpos.ui.staff.receptionist.order

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    /**----------------------------Order------------------------------------------*/
    fun addOrder(data: OrderEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addOrder(data)
        }
    }

    fun deleteOrder(data: OrderEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.deleteOrder(data)
        }
    }
    fun getOrder(orderId: String) = DatabaseUtil.getOrder(orderId)
    fun getOrderByTable(tableId: Int) = DatabaseUtil.getOrderByTable(tableId)
    fun getListOrderByCustomerId(customerId: Int) = DatabaseUtil.getListOrderByCustomerId(customerId)

    /**----------------------------Cart------------------------------------------*/
    fun addCartItem(data: CartItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addCartItem(data)
        }
    }

    fun addListCartItem(data: List<CartItemEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addListCartItem(data)
            Log.d("Quangdata", data.toString())
        }
    }

    fun getListCartItemByOrderId(orderId: String) = DatabaseUtil.getListCartItemByOrderId(orderId)
    fun getListCartItemByTableId(tableId: Int) = DatabaseUtil.getListCartItemByTableId(tableId)
    fun getListCartItemByTableIdAndOrderStatus(tableId: Int) = DatabaseUtil.getListCartItemByTableIdAndOrderStatus(tableId)

    fun getListCartItemOnWaiting(orderId: String) = DatabaseUtil.getListCartItemOnWaiting(orderId)

    fun getListCartItemOfKitchen() = DatabaseUtil.getListCartItemOfKitchen()

    fun getListCartItemOfKitchenBySortTime(sortByTimeOfOrder: Int) =
        DatabaseUtil.getListCartItemOfKitchenBySortTime(sortByTimeOfOrder)

    fun getListCartItemOfKitchenSortByTable(sortByTable: Int) =
        DatabaseUtil.getListCartItemOfKitchenSortByTable(sortByTable)

    fun getListCartItemOfKitchenSortByStatus(sortByStatus: Int) =
        DatabaseUtil.getListCartItemOfKitchenSortByStatus(sortByStatus)

    fun getListCartItemOfKitchenSortByItemName(sortByItemName: Int) =
        DatabaseUtil.getListCartItemOfKitchenSortByItemName(sortByItemName)

    fun getListCartItemOfKitchenSortByOrderQuantity(sortByOrderQuantity: Int) =
        DatabaseUtil.getListCartItemOfKitchenSortByOrderQuantity(sortByOrderQuantity)

    fun getListCartItemOfKitchenSortByNote(sortByNote: Int) =
        DatabaseUtil.getListCartItemOfKitchenSortByNote(sortByNote)


    fun deleteCartItem(data: CartItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.deleteCart(data)
        }
    }

}