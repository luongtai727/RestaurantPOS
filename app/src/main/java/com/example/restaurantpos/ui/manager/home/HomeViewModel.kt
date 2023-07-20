package com.example.restaurantpos.ui.manager.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _isDuplicate: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }

    val isDuplicate: LiveData<Float> = _isDuplicate

    fun getRevenueOfDayOfItem(id_item: Int, time: String) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.getRevenueOfDayOfItem(id_item, time)
        }
    }


/*    fun getRevenueOfDay(time: String) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.getRevenueOfDay(time)
        }
    }*/

    fun getRevenueOfDay(nowYear:Int, nowMonth: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            // Lấy số ngày ra
            val countDay = DataUtil.getNumberOfDayInMonth(nowYear, nowMonth)
            for (i in 1..countDay) {
                val amount = DatabaseUtil.getRevenueOfDay("$nowYear/$nowMonth/$i")

                // Gửi lên UI,đưa vào biểu đồ
                _isDuplicate.postValue(amount)
            }
            _isDuplicate.postValue(-1f)
        }
    }

}