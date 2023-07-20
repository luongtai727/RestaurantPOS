package com.example.restaurantpos.util

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.restaurantpos.db.dao.AccountDAO
import com.example.restaurantpos.db.dao.AppDAO
import com.example.restaurantpos.db.dao.CartItemDAO
import com.example.restaurantpos.db.dao.CategoryDAO
import com.example.restaurantpos.db.dao.CouponDAO
import com.example.restaurantpos.db.dao.CustomerDAO
import com.example.restaurantpos.db.dao.ItemDAO
import com.example.restaurantpos.db.dao.OrderDAO
import com.example.restaurantpos.db.dao.ShiftDAO
import com.example.restaurantpos.db.dao.TableDAO
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountRoleEntity
import com.example.restaurantpos.db.entity.AccountShiftEntity
import com.example.restaurantpos.db.entity.AccountStatusEntity
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.db.entity.CartItemStatusEntity
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.db.entity.CouponEntity
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.db.entity.OrderStatusEntity
import com.example.restaurantpos.db.entity.ShiftEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.db.entity.TableStatusEntity
import com.example.restaurantpos.db.roomdb.PosRoomDatabase

object DatabaseUtil {

    lateinit var appDAO: AppDAO
    lateinit var accountDAO: AccountDAO
    lateinit var categoryDAO: CategoryDAO
    lateinit var couponDAO: CouponDAO
    lateinit var itemDAO: ItemDAO
    lateinit var tableDAO: TableDAO
    lateinit var cartItemDAO: CartItemDAO
    lateinit var orderDAO: OrderDAO
    lateinit var customerDAO: CustomerDAO
    lateinit var shiftDAO: ShiftDAO
//    lateinit var tokenDAO: TokenDAO


    fun init(context: Context) {
        appDAO = PosRoomDatabase.getInstance(context).appDAO()
        accountDAO = PosRoomDatabase.getInstance(context).accountDAO()
        categoryDAO = PosRoomDatabase.getInstance(context).categoryDAO()
        couponDAO = PosRoomDatabase.getInstance(context).couponDAO()
        itemDAO = PosRoomDatabase.getInstance(context).itemDAO()
        tableDAO = PosRoomDatabase.getInstance(context).tableDAO()
        cartItemDAO = PosRoomDatabase.getInstance(context).cartItemDAO()
        orderDAO = PosRoomDatabase.getInstance(context).orderDAO()
        customerDAO = PosRoomDatabase.getInstance(context).customerDAO()
        shiftDAO = PosRoomDatabase.getInstance(context).shiftDAO()
//        tokenDAO = PosRoomDatabase.getInstance(context).tokenDAO()
    }

    /** 1. USER MANAGEMENT  */
    fun checkLogin(user_name: String, password: String) = accountDAO.checkLogin(user_name, password)
    fun addAccount(accountEntity: AccountEntity) = accountDAO.addAccount(accountEntity)

    fun addListAccount(listAccount: List<AccountEntity>) = accountDAO.addListAccount(listAccount)

    fun addListAccountRole(listAccountRole: List<AccountRoleEntity>) =
        accountDAO.addListAccountRole(listAccountRole)

    fun addListAccountStatus(listAccountStatus: List<AccountStatusEntity>) =
        accountDAO.addListAccountStatus(listAccountStatus)

    fun getAccountById(account_id: Int) = accountDAO.getAccountById(account_id)
    fun getAllUser() = accountDAO.getAllUser()
    fun getAllUserActive() = accountDAO.getAllUserActive()

    // Phục vụ cho việc add account_shift
    fun getAllUserActiveByName(name: String) =
        accountDAO.getAllUserActiveByName("%${name}%")

    fun getStaffByName(staffName: String) = accountDAO.getStaffByName("%${staffName}%")

    /** 2. CATEGORY && ITEM MANAGEMENT  */

    fun getAllCategory() = categoryDAO.getAllCategory()
    fun addCategory(data: CategoryEntity) = categoryDAO.addCategory(data)
    fun addCategoryItem(data: ItemEntity) = itemDAO.addCategoryItem(data)
    fun deleteItemOfCategory(itemOfCategory: ItemEntity) =
        itemDAO.deleteItemOfCategory(itemOfCategory)


    fun addListCategoryItem(listData: List<ItemEntity>) = itemDAO.addListCategoryItem(listData)
    fun getListCategoryComponentItem(categoryComponentId: Int) =
        itemDAO.getListCategoryComponentItem(categoryComponentId)

    fun getItemOfCategory(item_id: Int) = itemDAO.getItemOfCategory(item_id)
    fun getItemByName(name: String) = itemDAO.getItemByName(name)


    /** 3. TABLE MANAGEMENT  */
    fun addTable(data: TableEntity) = tableDAO.addTable(data)

    fun getTableById(table_id: Int) = tableDAO.getTableById(table_id)
    fun getAllTable() = tableDAO.getAllTable()


    fun addListTableStatus(listTableStatus: List<TableStatusEntity>) =
        tableDAO.addListTableStatus(listTableStatus)

    /** 4. CART MANAGEMENT  */
    fun addCartItem(data: CartItemEntity) = cartItemDAO.addCartItem(data)
    fun addListCartItem(data: List<CartItemEntity>) = cartItemDAO.addListCartItem(data)

    fun addListCartItemStatus(listCartItemStatus: List<CartItemStatusEntity>) =
        cartItemDAO.addListCartItemStatus(listCartItemStatus)

    fun deleteCart(data: CartItemEntity) = cartItemDAO.deleteCartItem(data)
    fun getListCartItemByOrderId(order_id: String) = cartItemDAO.getListCartItemByOrderId(order_id)
    fun getListCartItemByTableIdAndOrderStatus(tableId: Int) =
        cartItemDAO.getListCartItemByTableIdAndOrderStatus(tableId)

    fun getListCartItemByTableId(table_id: Int) = cartItemDAO.getListCartItemByTableId(table_id)

    // 2 hàm dưới làm gì?
    fun getListCartItemOfKitchen() = cartItemDAO.getListCartItemOfKitchen()
    fun getListCartItemOfKitchenBySortTime(sortByTimeOfOrder: Int) =
        cartItemDAO.getListCartItemOfKitchenBySortTime(sortByTimeOfOrder)
    fun getListCartItemOfKitchenSortByTable(sortByTable: Int) =
        cartItemDAO.getListCartItemOfKitchenSortByTable(sortByTable)
    fun getListCartItemOfKitchenSortByStatus(sortByStatus: Int) =
        cartItemDAO.getListCartItemOfKitchenSortByStatus(sortByStatus)
    fun getListCartItemOfKitchenSortByItemName(sortByItemName: Int) =
        cartItemDAO.getListCartItemOfKitchenSortByItemName(sortByItemName)
    fun getListCartItemOfKitchenSortByOrderQuantity(sortByOrderQuantity: Int) =
        cartItemDAO.getListCartItemOfKitchenSortByOrderQuantity(sortByOrderQuantity)
    fun getListCartItemOfKitchenSortByNote(sortByNote: Int) =
        cartItemDAO.getListCartItemOfKitchenSortByNote(sortByNote)

    fun getListCartItemOnWaiting(order_id: String) = cartItemDAO.getListCartItemOnWaiting(order_id)

    /** 5. ORDER MANAGEMENT  */

    fun addOrder(data: OrderEntity) = orderDAO.addOrder(data)


    fun addListOrderStatus(listOrderStatus: List<OrderStatusEntity>) =
        orderDAO.addListOrderStatus(listOrderStatus)


    fun deleteOrder(data: OrderEntity) = orderDAO.deleteOrder(data)
    fun getOrder(order_id: String) = orderDAO.getOrder(order_id)
    fun getOrderByTable(table_id: Int) = orderDAO.getOrderByTable(table_id)
    fun getListOrderByCustomerId(id: Int) = orderDAO.getListOrderByCustomerId(id)

    /** 6. CUSTOMER MANAGEMENT  */
    fun addCustomer(data: CustomerEntity) = customerDAO.addCustomer(data)

    fun deleteCustomer(data: CustomerEntity) = customerDAO.deleteCustomer(data)

    fun getCustomer(id: Int) = customerDAO.getCustomer(id)

    fun getListCustomer() = customerDAO.getListCustomer()

    // Phục vụ cho việc tìm kiếm Khách
    fun getListCustomerByPhoneForSearch(phone: String) =
        customerDAO.getListCustomerByPhoneForSearch("${phone}%")

    fun getListCustomerByPhoneForAdd(phone: String) =
        customerDAO.getListCustomerByPhoneForAdd(phone)

    /** 7. Shift && ShiftAccount MANAGEMENT  */
    fun addShift(shift: ShiftEntity) = shiftDAO.addShift(shift)


    fun addListShift(listShift: List<ShiftEntity>) = shiftDAO.addListShift(listShift)


    fun getShiftById(shift_id: String) = shiftDAO.getShiftById(shift_id)

    fun getListShift() = shiftDAO.getListShift()

    fun addAccountShift(accountShift: AccountShiftEntity) = shiftDAO.addAccountShift(accountShift)

    fun deleteAccountShift(accountShift: AccountShiftEntity) =
        shiftDAO.deleteAccountShift(accountShift)

    // Use for Shift's Show
    fun getListAccountShift(shift_id: String) = shiftDAO.getListAccountShift(shift_id)
    fun getListAccountShiftReceptionist(shift_id: String) =
        shiftDAO.getListAccountShiftReceptionist(shift_id)

    fun getListAccountShiftKitchen(shift_id: String) = shiftDAO.getListAccountShiftKitchen(shift_id)

    fun getListAccountShiftForSetListData(shift_id: String) =
        shiftDAO.getListAccountShiftForSetListData(shift_id)

    /** 8. TOKEN  */
//    fun addToken(data : TokenEntity) = tokenDAO.addToken(data)
//
//    fun deleteToken(data : TokenEntity) = tokenDAO.deleteToken(data)
//
//    fun checkToken(token: String, now : String) = tokenDAO.checkToken(token, now)
//    fun getAccountToken(token: String, now : String) = tokenDAO.getAccountToken(token, now)


    /** 9. Statistic  */
    fun getRevenueOfDay(time: String) = cartItemDAO.getRevenueOfDay("${time}%")

    fun getRevenueOfDayOfItem(id_item: Int, time: String) =
        cartItemDAO.getRevenueOfDayOfItem(id_item, "${time}%")

    /** 10. Checkout  */

    fun getSubTotal(orderId: String) = cartItemDAO.getSubTotal(orderId)

    /** 11. Coupon  */
    fun addCoupon(coupon: CouponEntity) = couponDAO.addCoupon(coupon)
    fun deleteCoupon(coupon: CouponEntity) = couponDAO.deleteCoupon(coupon)

    fun getAllCoupon() = couponDAO.getAllCoupon()

    fun getAllCouponActive() = couponDAO.getAllCouponActive()
    fun getCouponActiveByCouponCode(couponCode: String) = couponDAO.getCouponActiveByCouponCode(couponCode)
    fun getCouponByCouponCode(couponCode: String) = couponDAO.getCouponByCouponCode(couponCode)


}