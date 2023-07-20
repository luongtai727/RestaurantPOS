package com.example.restaurantpos.ui.manager.customer.bill

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

class ListCartItemOfCustomerOrderBillAdapter(
    private var context: Context,
    private var lifecycleOwner: LifecycleOwner,
    private var listData: MutableList<CartItemEntity>,
) : RecyclerView.Adapter<ListCartItemOfCustomerOrderBillAdapter.ViewHolder>() {

    private var moneyAmount = 0.0f

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtNo = itemView.findViewById<TextView>(R.id.txtNo)
        var txtItemName = itemView.findViewById<TextView>(R.id.txtItemName)
        var txtOrderQuantity = itemView.findViewById<TextView>(R.id.txtOrderQuantity)
        var txtItemPrice = itemView.findViewById<TextView>(R.id.txtItemPrice)
        var viewRootCartItemOnBillOfCustomer =
            itemView.findViewById<LinearLayout>(R.id.viewRootCartItemOnBillOfCustomer)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_item_on_bill_of_customer, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = listData[position]

        holder.txtNo.text = "${position + 1}"
        holder.txtOrderQuantity.text = cartItem.order_quantity.toString()
        // Tên, Giá sản phẩm lấy từ ItemEntity.
        showInfoOfItem(
            holder.txtItemName,
            holder.txtItemPrice,
            cartItem.item_id,
            cartItem.order_quantity
        )
    }


    /**--------------------------------------------------------------------------------------*/

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<CartItemEntity>) {
        listData.clear()
        moneyAmount = 0.0f
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    private fun showInfoOfItem(
        txtItemName: TextView,
        txtItemPrice: TextView,
        item_id: Int,
        orderQuantity: Int
    ) {
        DatabaseUtil.getItemOfCategory(item_id).observe(lifecycleOwner) { item ->
            txtItemName.text = item[0].item_name
            txtItemPrice.text = item[0].price.toString()
            moneyAmount += orderQuantity * item[0].price
            // Tính được tổng tiền của 1 Item. Sau chạy For tổng lại là xong
        }
    }

    // Biến tấu như thằng listData bên trên luôn
    // Nó bị biến tấu như cái listData nhưng chỉ là mình không cần truyền cái ListData này vào thôi
    // Cứ mỗi lần get ra ItemCount thì nó bị biến tấu theo thằng List
    // Mà thằng List thì cũng được thay đổi khi mình setData
    // Tái tạo lại thằng showInfo() luôn. Chạy ÍT lần lại.
    // Chứ cứ gọi showInfo() ra nữa thì bị tốn tài nguyên.
    /**Trong khi mình có thể sử dụng thẳng  adapter.getMoneyAmountOfOneItem() */

    private fun getMoneyAmountOfOneItem() = moneyAmount
}