package com.example.restaurantpos.ui.staff.receptionist.order.newOrder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.ItemEntity


/** Adapter mày: CategoryItem --> ItemOfCategory ở phía dưới OrderFragment*/
class ItemOfCategoryInBottomOfOrderFragmentAdapter(
    var context: Context,
    private var listData: MutableList<ItemEntity>,
    val listenerClickOrderItem: EventClickOrderItemListener


) : RecyclerView.Adapter<ItemOfCategoryInBottomOfOrderFragmentAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewRootOrderItem = itemView.findViewById<LinearLayout>(R.id.viewRootOrderItem)
        var imgCategoryItemImage = itemView.findViewById<ImageView>(R.id.imgCategoryItemImage)
        var txtItemName = itemView.findViewById<TextView>(R.id.txtItemName)
        var txtItemPrice = itemView.findViewById<TextView>(R.id.txtItemPrice)
        var txtItemInventoryQuantity =
            itemView.findViewById<TextView>(R.id.txtItemInventoryQuantity)
        var btnAdd = itemView.findViewById<Button>(R.id.btnAdd)

    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_item, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount() = listData.size


    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemInCategory = listData[position]
        /** Đổ Data cho OrderItem */
        holder.imgCategoryItemImage.setImageBitmap(BitmapFactory.decodeFile(itemInCategory.image))
        holder.txtItemName.text = itemInCategory.item_name
        holder.txtItemPrice.text = String.format("%.1f", itemInCategory.price) + " $"
        holder.txtItemInventoryQuantity.text = itemInCategory.inventory_quantity.toString()

        /** Add OrderItem to CART */
        holder.btnAdd.setOnClickListener {
            listenerClickOrderItem.clickAddOrderItem(itemInCategory)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<ItemEntity>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    interface EventClickOrderItemListener {
        fun clickAddOrderItem(itemInCategory: ItemEntity)
    }
}