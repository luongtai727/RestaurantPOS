package com.example.restaurantpos.ui.manager.home.shift

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.DatabaseUtil
import com.example.restaurantpos.util.DateFormatUtil

// Thằng này xử lý đâu đầu vãi
class ShiftAdapter(
    var context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private var listData: MutableList<String>,
    private var year: Int,
    private var month: Int,
    private var day: Int,
    // Mình cần ngày bắt đầu của tuần
    val listenerClickShift: EventClickShiftListener
) : RecyclerView.Adapter<ShiftAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtDay = itemView.findViewById<TextView>(R.id.txtDay)
        var txtDayOfWeek = itemView.findViewById<TextView>(R.id.txtDayOfWeek)
        var txtMorningShift = itemView.findViewById<TextView>(R.id.txtMorningShift)
        var txtAfternoonShift = itemView.findViewById<TextView>(R.id.txtAfternoonShift)
        var txtNightShift = itemView.findViewById<TextView>(R.id.txtNightShift)
    }

    override fun getItemCount(): Int = 7

    /*    override fun getItemCount(): Int {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                return numberOfDayInAMonthOfLeapYear[month]
            } else {
                return numberOfDayInAMonthOfNotLeapYear[month]
            }
        }

        // Mỗi tháng có bao nhiêu ngày, còn cả vụ nhăm nhuận và start từ 0
        val numberOfDayInAMonthOfNotLeapYear =
            listOf<Int>(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val numberOfDayInAMonthOfLeapYear =
            listOf<Int>(0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)*/

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shift, parent, false)
        return ViewHolder(convertedView)
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    // Position ở đây sẽ ứng dụng position của List trong Database
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val accountShift = listData[position]
        /*        private var year = 2023
                private var month = 7
                private var day = 4*/
        /** Đổ data lên View */
        // Mình sẽ truyền vào ngày đầu tiên của tuần
        // Xíu nữa sẽ xử lý vấn đề tuần bắt đầu từ ngày bao nhiêu
//        holder.txtDay.text = "${day + position}"

        val day_pos = DataUtil.plusDayReturnDay(year, month, day, position)
        val month_pos = DataUtil.plusDayReturnMonth(year, month, day, position)
        val year_pos = DataUtil.plusDayReturnYear(year, month, day, position)

        // Này đang sai
        holder.txtDay.text = "$month_pos/$day_pos"
        // Này chuẩn
        holder.txtDayOfWeek.text = when (position) {
            0 -> "Mon"
            1 -> "Tue"
            2 -> "Wed"
            3 -> "Thur"
            4 -> "Fri"
            5 -> "Sat"
            6 -> "Sun"
            else -> "Mon"
        }

        /*
        Shift_name
        1: Morning
        2: Afternoon
        3: Night
        */

        val morningShiftID = DateFormatUtil.getShiftId(year_pos, month_pos, day_pos, 1)
        val afternoonShiftID = DateFormatUtil.getShiftId(year_pos, month_pos, day_pos, 2)
        val nightShiftID = DateFormatUtil.getShiftId(year_pos, month_pos, day_pos, 3)

        // Show Account Shift on Shift
        showMorningShift(holder.txtMorningShift, morningShiftID)
        showAfternoonShift(holder.txtAfternoonShift, afternoonShiftID)
        showNightShift(holder.txtNightShift, nightShiftID)

        /** Xử lý sự kiện khi click vào Shift */
        holder.txtMorningShift.setOnClickListener {
            listenerClickShift.clickMorningShift(morningShiftID)
        }

        holder.txtAfternoonShift.setOnClickListener {
            listenerClickShift.clickAfternoonShift(afternoonShiftID)
        }

        holder.txtNightShift.setOnClickListener {
            listenerClickShift.clickNightShift(nightShiftID)
        }


    }

    /**-----------------------------------------------------------------------------------------*/
    private fun showMorningShift(txtMorningShift: TextView, shiftID: String) {
        DatabaseUtil.getListAccountShift(shiftID).observe(lifecycleOwner) { listAccountName ->
//            val listAccountNameShow = setListData(listAccountName)
//            txtMorningShift.text = listAccountNameShow.toString()

            showInfo(txtMorningShift, listAccountName as ArrayList<String>)
        }
    }

    private fun showAfternoonShift(txtAfternoonShift: TextView, shiftID: String) {
        DatabaseUtil.getListAccountShift(shiftID).observe(lifecycleOwner) { listAccountName ->
//            val listAccountNameShow = setListData(listAccountName)
//            txtAfternoonShift.text = listAccountNameShow.toString()
            showInfo(txtAfternoonShift, listAccountName as ArrayList<String>)
        }
    }

    private fun showNightShift(txtNightShift: TextView, shiftID: String) {
        DatabaseUtil.getListAccountShift(shiftID).observe(lifecycleOwner) { listAccountName ->
//            val listAccountNameShow = setListData(listAccountName)
//            txtNightShift.text = listAccountNameShow.toString()
            showInfo(txtNightShift, listAccountName as ArrayList<String>)
        }
    }

    // ShowInfo --> Show ở đâu (txtViewOfShift), show cái gì (listAccountName)
    private fun showInfo(txtViewOfShift: TextView, listAccountName: ArrayList<String>) {
        var showOnShift = "・ "
        listAccountName.forEach { accountName ->
            showOnShift += "$accountName\n ・"
        }
        txtViewOfShift.text = showOnShift
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<String>): MutableList<String> {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
        return listData
    }


    interface EventClickShiftListener {
        // day = position + 1
        // Click là addAccountShift luôn
        fun clickMorningShift(shift_id: String)
        fun clickAfternoonShift(shift_id: String)
        fun clickNightShift(shift_id: String)
    }
}

/*
1.  Day: Ngày bắt đầu của tuần
2. Xử lý thế nào để được ngày nữa cơ
 */