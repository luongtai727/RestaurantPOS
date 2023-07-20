package com.example.restaurantpos.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AppDAO {

    /** Checkout */
    @Query("SELECT SUM(cart_item.order_quantity * item.price) FROM cart_item JOIN `order` ON `order`.order_id = cart_item.order_id JOIN item ON cart_item.item_id = item.item_id   WHERE `order`.order_id = :orderID")
    fun getSubTotal(orderID: String): Float


//    @Query("SELECT item.price FROM `order` JOIN cart_item ON `order`.order_id = cart_item.order_id JOIN item ON cart_item.item_id = item.item_id   WHERE `order`.order_id = :orderID")
//    fun getSubTotal(orderID: String): List<Float>
//    @Query("SELECT `order`.table_id FROM `order`  WHERE `order`.order_id = :orderID")
//    fun getSubTotal(orderID: String): Int




    /** Revenue */

    //Doanh thu theo ngày, của TOÀN BỘ: 2023/07/11 -> 2023 (của năm), 2023/07 (của tháng), 2023/07/11 (của ngày) ==> Tùy time truyền vào là lụm ra filter ưng ý
    @Query("SELECT SUM(cart_item.order_quantity * item.price) FROM `order` JOIN cart_item ON `order`.order_id = cart_item.order_id JOIN item ON cart_item.item_id = item.item_id   WHERE `order`.order_create_time LIKE :time")
    fun getRevenueOfDay(time: String): Float

    //Doanh thu theo ngày, của Item: Đưa vào Item và ngày rồi lọc ra
    // 2023/07/11-> 2023/07/11 (time --> Dùng LIKE)
    // Lọc ra Doanh thu (Order) theo ngày (Order Time)
    // Cart chứa toàn bộ món ăn đã được order  ==> Lọc ra THẰNG ITEM mà mình muốn thống kê
    // Join tiếp với item để lấy giá ra.
    @Query("SELECT SUM(cart_item.order_quantity * item.price) FROM `order` Join cart_item ON `order`.order_id = cart_item.order_id JOIN item ON cart_item.item_id = item.item_id  WHERE cart_item.item_id = :id_item AND `order`.order_create_time LIKE :time")
    fun getRevenueOfDayOfItem(id_item: Int, time: String): Float


    /** Join Table (Select) */
    // ON: Điều kiện Join --> Nhìn giữa 2 thằng xem thằng nào bằng thằng nào.
    // Không cần when nữa.
    // Lúc này phải đi tạo ra 1 entỉty mới (Class dạng data) để hứng dữ liệu get được
    // CHÚ Ý: Không cần thêm mow @Entity(tableName = "xxx"). chỉ cần là data class, là okay
    // Data Class là đủ điều kiện để hứng (trong method get- QUERY) data từ Server  or Database mình get ra rồi.
    // Rõ vấn đề nha: Mình truy cập vào bảng order và order chứ không truy cập vào TableOrderEntity
    //    TableOrderEntity là hứng lại thôi, để chứa thằng get được thôi nha.
    // Gắn mớ @Entity(tableName = "xxx") này vào
    // Cũng không cần Parcelable (có cũng okay), constructor luôn. Chừa lại Gson okay, để đáp qua đáp lại.
    // Giờ phải đi copy order và table Entity vào để đủ trường mà hứng
    // Ví dụ status chung tên, méo biết get của thằng nào nữa cơ. --> Phải phân tách bọn nó ra cơ
    // Rồi câu lệnh get dưới trả về bảng vừa tạo thay vì MemeEntity --> Bị gấp đối số lượng bảng
    // Lúc mới bắt đầu học SQL, cú phải chạy đi join join hết các kiểu.
    // Nhưng thực tế mới nhận ra. Đéo cần join vẫn làm được như thường. Đi làm mới biết nó thừa thải vãi ra

    // Học SQL không phải chỉ để thuộc câu lệnh. Mà quan trọng là hiểu bản chất của việc Xử lý Dữ liệu
    // SQL là nền tảng của rất nhiều hệ thông quản lý.
    // Kể cả Backend cũng xử lý thuần SQL hết chứ không dùng Room thế này đâu
    // Như mình hiện tại gọi là Fake of Life
    // Mình giao tiếp với bọn code BackEnd (Kể cả Backend Apply như C# hay Java dùng window Form
    // Kiểu mấy hệ thống quản lý của cửa hàng bán hàng ấy ... đều sử dụng thuần SQL hết
    // Tất nhiên trong SQL cũng có rất nhiều dạng. Mỗi thằng có những đặc trưng riêng nhưng về mặt bản chất nó giống nhau
    // Window Form / JavaJVC,... bọn này thuần SQL lắm luôn: Connect, Nạp thư viện SQL, Chạy tay confix bằng mấy cái SQL Connection

    // Vậy việc gộp này để lưu dữ liệu lại, khi cần thì export file ra, hay gì đó thôi
    // Data Class dùng để hứng từ CSDL và Call Database

    /*
        @Query("SELECT * FROM `order` JOIN `table` ON `order`.table_id = `table`.table_id")
        fun getAllMeme(): MutableList<TableOrderEntity>
    */


    /*
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun addAccount(account: AccountEntity): Long
    */


    /*
@Query("SELECT * FROM meme")
fun getAllMeme(): MutableList<MemeEntity>

@Insert(onConflict = OnConflictStrategy.REPLACE)
fun addAllMeme(listData: ArrayList<MemeEntity>): List<Long>

@Insert(onConflict = OnConflictStrategy.REPLACE)
fun addUser(userEntity: UserEntity): Long

@Query("delete from meme where id in (:listData)")
fun deleteAllMeme(listData: List<String>)

@Delete
fun deleteUser(userEntity: UserEntity): Int
*/

}