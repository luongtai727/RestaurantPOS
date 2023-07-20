package com.example.restaurantpos.ui.manager.home.shift

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.AccountEntity

class StaffSelectionAdapter(
    var context: Fragment,
    private var listData: MutableList<AccountEntity>,
    val listenerClickStaff: EventClickStaffListener
) : RecyclerView.Adapter<StaffSelectionAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtStaffName = itemView.findViewById<TextView>(R.id.txtStaffName)
        var txtStaffRole = itemView.findViewById<TextView>(R.id.txtStaffRole)
        var viewRootStaff = itemView.findViewById<LinearLayout>(R.id.viewRootStaff)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_staff_selection, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemStaff = listData[position]

        holder.txtStaffName.text = itemStaff.account_name
        holder.txtStaffRole.text =
            when (itemStaff.role_id) {
                0 -> "Manager"
                1 -> "Receptionist"
                2 -> "Kitchen"
                else -> "Error"
            }

        holder.viewRootStaff.setOnClickListener {
            listenerClickStaff.clickStaff(itemStaff)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<AccountEntity>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    interface EventClickStaffListener {
        fun clickStaff(itemStaff: AccountEntity)
    }
}