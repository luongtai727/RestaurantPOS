//package com.example.restaurantpos.db.dao
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.restaurantpos.db.entity.TokenEntity
//
//@Dao
//interface TokenDAO {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addToken(data: TokenEntity): Long
//
//    @Delete
//    fun deleteToken(data: TokenEntity): Int
//
//    /*
//    Đếm xem có trả ra thằng nào không. Nếu là 0 thì time hết hạn
//    Vừa liên quan đến bảo mật, vừa liên quan đén quyền truy cập
//    Max hóa pass sau mỗi lần đăng nhập là được ấy mà
//    Thực chất thì nếu không làm thẻ token này, thì việc mã hóa thành vô nghĩa.
//    Vì của mình là offline. Nên mới cần thêm cái token vào thôi
//    Có những loại token có hạn, không có hạn, hoặc là không nhất thiết cần token
//
//
//    now: Thời điểm hiện tại. Check xem lúc đó token còn hạn hay không. Bỏ now đi thì token thành vô thời hạn
//         Cứ mỗi lần đăng nhập lại được 1 cái token khác, trong trường hợp cần truy xuất ngược
//    :now <= date  : Kiểu so sánh thời gian loại String
//
//    Trả ra 0 --> Logout nó luôn, bắt nó đăng nhập lại
//    Kiểu không để người dùng treo app quá lâu. VD: Cứ 2 tiếng bắt user nhập lại mật khẩu 1 lần
//     */
//    @Query("SELECT COUNT(*) FROM token_table WHERE token = :token AND :now <= date")
//    fun checkToken(token: String, now: String): Int
//
//    @Query("SELECT account.role_id FROM token_table join account on account.account_id = token_table.account_id WHERE token = :token AND :now <= date")
//    fun getAccountToken(token: String, now: String): Int
//
//
//}