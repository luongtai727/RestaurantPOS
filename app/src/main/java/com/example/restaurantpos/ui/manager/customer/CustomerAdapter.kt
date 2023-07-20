package com.example.restaurantpos.ui.manager.customer

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
import com.example.restaurantpos.db.entity.CustomerEntity

class CustomerAdapter(
    var context: Fragment,
    private var listData: MutableList<CustomerEntity>,
    val listenerClickCustomer: EventClickItemCustomerListener
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtCustomerName = itemView.findViewById<TextView>(R.id.txtCustomerName)
        var txtCustomerPhone = itemView.findViewById<TextView>(R.id.txtCustomerPhone)
        var txtCustomerBirthday = itemView.findViewById<TextView>(R.id.txtCustomerBirthday)
        var viewRootCustomer = itemView.findViewById<LinearLayout>(R.id.viewRootCustomer)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCustomer = listData[position]

        holder.txtCustomerName.text = itemCustomer.customer_name
        holder.txtCustomerPhone.text = itemCustomer.phone
        holder.txtCustomerBirthday.text = itemCustomer.birthday


        holder.viewRootCustomer.setOnClickListener {
            listenerClickCustomer.clickCustomer(itemCustomer)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<CustomerEntity>) {
        listData.clear()
        // Khách nào không muốn lưu lại thì dùng Unkown thôi.
        listData.add(CustomerEntity(0, "Unkown","...","...", 0.0))
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    interface EventClickItemCustomerListener {
        fun clickCustomer(itemCustomer: CustomerEntity)
    }
}