package com.example.restaurantpos.ui.staff.receptionist.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.CustomerEntity

class CustomerInnerAdapter (
    var context: Fragment,
    private var listData: MutableList<CustomerEntity>,
    val listenerClickCustomerInner: EventClickItemCustomerInnerListener
) : RecyclerView.Adapter<CustomerInnerAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtCustomerName = itemView.findViewById<TextView>(R.id.txtCustomerName)
        var txtCustomerPhoneInner = itemView.findViewById<TextView>(R.id.txtCustomerPhoneInner)
        var viewRootCustomerInner = itemView.findViewById<LinearLayout>(R.id.viewRootCustomerInner)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_in_phone, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCustomer = listData[position]

        holder.txtCustomerName.text = itemCustomer.customer_name
        holder.txtCustomerPhoneInner.text = itemCustomer.phone

        holder.viewRootCustomerInner.setOnClickListener {
            listenerClickCustomerInner.clickCustomerInner(itemCustomer)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<CustomerEntity>) {
        listData = newListData
        notifyDataSetChanged()
    }

    interface EventClickItemCustomerInnerListener {
        fun clickCustomerInner (itemCustomer: CustomerEntity)
    }
}