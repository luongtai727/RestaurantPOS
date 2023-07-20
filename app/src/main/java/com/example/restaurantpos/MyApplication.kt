package com.example.restaurantpos

import android.app.Application
import com.example.restaurantpos.util.DatabaseUtil
import com.example.restaurantpos.util.SharedPreferencesUtils

// Được khởi tạo đầu tiên và luôn luôn sống --> Hỗ trợ mình trong việc khai báo
// Qua Manifest khai báo trường name!
// Quản lý cả app luôn --> To hơn cả activity --> Không thể null được. Null là app méo tồn tại rồi
class MyApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        DatabaseUtil.init(this)
        SharedPreferencesUtils.init(this)

    }
}