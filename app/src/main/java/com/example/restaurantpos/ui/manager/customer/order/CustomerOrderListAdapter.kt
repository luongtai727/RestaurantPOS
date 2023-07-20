package com.example.restaurantpos.ui.manager.customer.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.OrderEntity

class CustomerOrderAdapter(
    var context: Fragment,
    private var listData: MutableList<OrderEntity>,
    val listenerClickCustomerDetailByOrder: EventClickCustomerDetailByOrderListener
) : RecyclerView.Adapter<CustomerOrderAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtNo = itemView.findViewById<TextView>(R.id.txtNo)
        var txtDate = itemView.findViewById<TextView>(R.id.txtDate)
        var imgCustomerBill = itemView.findViewById<ImageView>(R.id.imgCustomerBill)
        var viewRootCustomerBill = itemView.findViewById<LinearLayout>(R.id.viewRootCustomerBill)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_bill_history, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = listData[position]

        holder.txtNo.text = "${position + 1}"
        holder.txtDate.text = order.order_create_time

//
//        holder.viewRootCustomerBill.setOnClickListener {
//            listenerClickCustomerBill.clickBillDetail(order)
//        }
//

        holder.imgCustomerBill.setOnClickListener {
            listenerClickCustomerDetailByOrder.clickOrderofCustomer(order)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<OrderEntity>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    interface EventClickCustomerDetailByOrderListener {
        fun clickOrderofCustomer (order: OrderEntity)
    }
}