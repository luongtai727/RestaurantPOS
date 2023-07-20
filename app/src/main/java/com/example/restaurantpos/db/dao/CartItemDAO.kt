package com.example.restaurantpos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.db.entity.CartItemStatusEntity

@Dao
interface CartItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCartItem(data: CartItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListCartItem(data: List<CartItemEntity>): List<Long>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListCartItemStatus(listCartItemStatus: List<CartItemStatusEntity>): List<Long>

    @Delete
    fun deleteCartItem(data: CartItemEntity): Int

    // Get ListOrder Of OrderedTable, By order_id (Bill)

    @Query("SELECT * FROM `order`  JOIN cart_item  ON (`order`.order_id = cart_item .order_id) JOIN `table`  ON (`order`.table_id = `table`.table_id) WHERE `table`.table_id = :tableId AND `order`.order_status_id = 1")
    fun getListCartItemByTableIdAndOrderStatus(tableId: Int): LiveData<MutableList<CartItemEntity>>

    @Query("SELECT * FROM cart_item WHERE order_id = :order_id")
    fun getListCartItemByOrderId(order_id: String): LiveData<MutableList<CartItemEntity>>

    //    cart_item_status = 0: Những thứ vẫn chưa làm thì cho phép Edit/Delete
    @Query("SELECT * FROM cart_item WHERE order_id = :order_id AND cart_item_status_id = 0")
    fun getListCartItemOnWaiting(order_id: String): LiveData<MutableList<CartItemEntity>>

    @Query("SELECT * FROM cart_item WHERE cart_item_status_id < 4")
    fun getListCartItemOfKitchen(): LiveData<MutableList<CartItemEntity>>

    // Đỉnh: Case ở đây chính là If-Else
    /*
    Sort theo Order_id (Order_create_id)
    sortByTimeOfOrder = 0 --> Không Sort/Giữ nguyên tăng dần      Ascending
    sortByTimeOfOrder = 1 --> Sort ngược (Giảm dần)               Descending
    sortByTimeOfOrder = 2 --> Bỏ qua

    Bỏ qua thằng 2. Vì thường thì call API từ server về thì mới cần trạng thái Không Sort
    Chỉ có những thằng sort không theo thời gian thì mới có trạng thái không sort thôi

    Status cũng Livedata, kiểu LiveData kép ấy

    Con số status có thể quyết định việc bỏ đi hoặc không.
    SORT theo order_id chính là time luôn. (<-- Đổi order_id thành dạng time)
    Nhưng rốt cuộc thì mình lại thêm 1 trường time (cart_order_time) vào rồi, vậy nên quả order_id đâu cần phải dạng time làm gì nữa đâu.
    */
    @Query(
        "SELECT * FROM cart_item WHERE cart_item_status_id < 2  \n" +
                "ORDER BY \n" +
                "CASE WHEN :sortByTimeOfOrder = 0 THEN cart_order_time END DESC, \n" +
                "CASE WHEN :sortByTimeOfOrder = 1 THEN cart_order_time END ASC"
    )
    fun getListCartItemOfKitchenBySortTime(sortByTimeOfOrder: Int): LiveData<MutableList<CartItemEntity>>
    @Query(
        "SELECT * FROM cart_item JOIN `order` ON cart_item.order_id = `order`.order_id JOIN `table` ON `table`.table_id = `order`.table_id WHERE cart_item_status_id < 2  \n" +
                "ORDER BY \n" +
                "CASE WHEN :sortByTable = 0 THEN `order`.table_id END DESC, \n" +
                "CASE WHEN :sortByTable = 1 THEN `order`.table_id END ASC"
    )
    fun getListCartItemOfKitchenSortByTable(sortByTable: Int): LiveData<MutableList<CartItemEntity>>
    @Query(
        "SELECT * FROM cart_item WHERE cart_item_status_id < 2  \n" +
                "ORDER BY \n" +
                "CASE WHEN :sortByStatus = 0 THEN cart_item_status_id END DESC, \n" +
                "CASE WHEN :sortByStatus = 1 THEN cart_item_status_id END ASC"
    )
    fun getListCartItemOfKitchenSortByStatus(sortByStatus: Int): LiveData<MutableList<CartItemEntity>>
    @Query(
        "SELECT * FROM cart_item JOIN item ON cart_item.item_id = item.item_id WHERE cart_item_status_id < 2  \n" +
                "ORDER BY \n" +
                "CASE WHEN :sortByItemName = 0 THEN item.item_name END DESC, \n" +
                "CASE WHEN :sortByItemName = 1 THEN item.item_name END ASC"
    )
    fun getListCartItemOfKitchenSortByItemName(sortByItemName: Int): LiveData<MutableList<CartItemEntity>>

    @Query(
        "SELECT * FROM cart_item WHERE cart_item_status_id < 2  \n" +
                "ORDER BY \n" +
                "CASE WHEN :sortByOrderQuantity = 0 THEN order_quantity END DESC, \n" +
                "CASE WHEN :sortByOrderQuantity = 1 THEN order_quantity END ASC"
    )
    fun getListCartItemOfKitchenSortByOrderQuantity(sortByOrderQuantity: Int): LiveData<MutableList<CartItemEntity>>
    @Query(
        "SELECT * FROM cart_item WHERE cart_item_status_id < 2  \n" +
                "ORDER BY \n" +
                "CASE WHEN :sortByNote = 0 THEN note END DESC, \n" +
                "CASE WHEN :sortByNote = 1 THEN note END ASC"
    )
    fun getListCartItemOfKitchenSortByNote(sortByNote: Int): LiveData<MutableList<CartItemEntity>>

    @Query("SELECT * FROM `order`  JOIN cart_item  ON (`order`.order_id = cart_item .order_id) JOIN `table`  ON (`order`.table_id = `table`.table_id) WHERE `table`.table_id = :tableId")
    fun getListCartItemByTableId(tableId: Int): LiveData<MutableList<CartItemEntity>>


    @Query("SELECT SUM(cart_item.order_quantity * item.price) FROM cart_item JOIN `order` ON `order`.order_id = cart_item.order_id JOIN item ON cart_item.item_id = item.item_id   WHERE `order`.order_id = :orderID AND `order`.order_status_id = 1")
    fun getSubTotal(orderID: String): Float

    /** Revenue */
    @Query("SELECT SUM(cart_item.order_quantity * item.price) FROM `order` JOIN cart_item ON `order`.order_id = cart_item.order_id JOIN item ON cart_item.item_id = item.item_id   WHERE `order`.order_create_time LIKE :time")
    fun getRevenueOfDay(time: String): Float


    @Query("SELECT SUM(cart_item.order_quantity * item.price) FROM `order` Join cart_item ON `order`.order_id = cart_item.order_id JOIN item ON cart_item.item_id = item.item_id  WHERE cart_item.item_id = :id_item AND `order`.order_create_time LIKE :time")
    fun getRevenueOfDayOfItem(id_item: Int, time: String): Float

}