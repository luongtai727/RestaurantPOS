package com.example.restaurantpos.ui.manager.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _revenue : MutableLiveData<MutableList<Float>> by lazy {
        MutableLiveData<MutableList<Float>>()
    }

    val revenue: LiveData<MutableList<Float>> = _revenue



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
            val countDay = DataUtil.getNumberOfDayInMonth(nowYear, nowMonth)
            val listRevenue = ArrayList<Float>()
            val monthString = if (nowMonth < 10) "0$nowMonth" else "$nowMonth"

            for (i in 1..countDay) {
                val dayString = if (i < 10) "0$i" else "$i"

                val amount = DatabaseUtil.getAllOrder("$nowYear/$monthString/$dayString")
                listRevenue.add(amount)
            }

            _revenue.postValue(listRevenue)
        }
    }

}