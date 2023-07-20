package com.example.restaurantpos.ui.staff.receptionist.order.oldOrder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.util.DatabaseUtil

class CartItemInOldOrderAdapter(
    var context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private var listData: MutableList<CartItemEntity>,
    val listenerClickCartItemInOldOrder: EventClickCartItemInOldOrderListener
) : RecyclerView.Adapter<CartItemInOldOrderAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewRootCartItemInOldOrder =
            itemView.findViewById<LinearLayout>(R.id.viewRootCartItemInOldOrder)
        var txtNo = itemView.findViewById<TextView>(R.id.txtNo)
        var txtItemName = itemView.findViewById<TextView>(R.id.txtItemName)
        var txtOrderQuantity = itemView.findViewById<TextView>(R.id.txtOrderQuantity)
        var txtNote = itemView.findViewById<TextView>(R.id.txtNote)
        var txtCartItemStatus = itemView.findViewById<TextView>(R.id.txtCartItemStatus)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_item_detail_in_old_order, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    // Position ở đây sẽ ứng dụng position của List trong Database
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = listData[position]
        /** Đổ data lên View */
        holder.txtNo.text = "${position + 1}"
        holder.txtOrderQuantity.text = cartItem.order_quantity.toString()
        holder.txtNote.text = cartItem.note

        holder.txtCartItemStatus.setOnClickListener {
            if (cartItem.cart_item_status_id == 2) {
                listenerClickCartItemInOldOrder.clickCartItemServedStatus(cartItem)
            }
        }

        holder.viewRootCartItemInOldOrder.setOnLongClickListener {
            if (cartItem.cart_item_status_id == 0) {
                listenerClickCartItemInOldOrder.deleteItemCanceledByCustomer(cartItem)
            }
            true
        }

        showInfo(
            holder.txtItemName,
            holder.txtCartItemStatus,
            cartItem.cart_item_status_id,
            cartItem.item_id
        )
    }

    // Những thông tin về Table, Item thì phải get bằng order_id, item_id nha
    private fun showInfo(txtItemName: TextView, txtCartItemStatus: TextView, status: Int, item_id: Int) {
        // Item
        DatabaseUtil.getItemOfCategory(item_id).observe(lifecycleOwner) { itemList ->
            txtItemName.text = itemList[0].item_name
        }

        when (status) {
            0 -> {
                txtCartItemStatus.text = "Waiting"
                txtCartItemStatus.setBackgroundResource(R.drawable.bg_status_red)
            }

            1 -> {
                txtCartItemStatus.text = "In Process"
                txtCartItemStatus.setBackgroundResource(R.drawable.bg_status_gray)
            }

            2 -> {
                txtCartItemStatus.text = "Done"
                txtCartItemStatus.setBackgroundResource(R.drawable.bg_status_green)
            }

            3 -> {
                txtCartItemStatus.text = "Served"
                txtCartItemStatus.setBackgroundResource(R.drawable.bg_status_white)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<CartItemEntity>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }


    interface EventClickCartItemInOldOrderListener {
        fun clickCartItemServedStatus(cartItemInOldOrder: CartItemEntity)

        fun deleteItemCanceledByCustomer(cartItemInOldOrder: CartItemEntity)
    }
}
