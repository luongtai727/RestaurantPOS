package com.example.restaurantpos.ui.manager.home.shift

import android.accounts.Account
import com.example.restaurantpos.db.entity.AccountShiftEntity


/*
 Vì AccountShiftEntity sinh ra, không thể tự lọc ra theo từng ca mà mình muốn được
Có nghĩa là theo từng ngày mà mình muốn ấy
Kiếm công thức gì đấy mà mình lọc 1 phát ra luôn

Bản thân khi get List ra là của cả 1 tháng, không thể phân biệt được
Nên giờ mình sẽ lọc ra theo ngày
*/

class ShiftModel(
    var day: Int,
    val listAccountShift: ArrayList<AccountShiftEntity>
) {
}