package com.example.restaurantpos.ui.staff.receptionist.checkout

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.util.DatabaseUtil

class ItemCheckoutAdapter(
    var context: Context,
    private var listData: ArrayList<CartItemEntity>,
    var lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ItemCheckoutAdapter.ViewHolder>() {

    private var costOfItem = 0f;

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtNo: TextView = view.findViewById(R.id.txtNo)
        var txtItemName: TextView = view.findViewById(R.id.txtItemName)
        var txtOrderQuantity: TextView = view.findViewById(R.id.txtOrderQuantity)
        var txtItemPrice: TextView = view.findViewById(R.id.txtItemPrice)
        var txtCost: TextView = view.findViewById(R.id.txtCost)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_cart_item_detail_in_bill, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val itemInBill = listData[position]


        viewHolder.txtNo.text = "${position + 1}"
        viewHolder.txtOrderQuantity.text = itemInBill.order_quantity.toString()

        showInfor(
            viewHolder.txtItemName,
            viewHolder.txtItemPrice,
            itemInBill.order_quantity,
            viewHolder.txtCost,
            itemInBill.item_id
        )
    }


    @SuppressLint("SetTextI18n")
    fun showInfor(
        txtItemName: TextView,
        txtItemPrice: TextView,
        orderQuantity: Int,
        txtCost: TextView,
        item_id: Int
    ) {
        DatabaseUtil.getItemOfCategory(item_id).observe(lifecycleOwner) { listItem ->
            txtItemName.text = listItem[0].item_name
            txtItemPrice.text = String.format("%.1f", listItem[0].price) + " $"
            txtCost.text = String.format("%.1f", (orderQuantity * listItem[0].price)) + " $"
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListData(arr: ArrayList<CartItemEntity>) {
        listData.clear()
        listData.addAll(arr)
        notifyDataSetChanged()
        Log.d("Quanglt", "${listData.size}")
    }

    override fun getItemCount() = listData.size

}