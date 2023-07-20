package com.example.restaurantpos.ui.staff.receptionist.order.newOrder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.util.DatabaseUtil

/** Adapter mày: Category --> Category ở phía dưới OrderFragment*/
class CategoryInBottomOfOrderFragmentAdapter(
    var context: Context,
    private var listData: MutableList<CategoryEntity>,
    /**chooseCategory: Tương ứng với category_id*/
    /*
1. Foods
2. Drinks
3. Desserts
 */
    private var chosenCategory: Int = 1,
    /**observe khong get được nên tạo biên trên này*/
    private val lifecycleOwner: LifecycleOwner,
    /**Nên đặt này ở cuối để khi sang Fragment dễ nhìn hơn*/
    val listenerClickCategoryInOrder: EventClickCategoryInOrderListener


) : RecyclerView.Adapter<CategoryInBottomOfOrderFragmentAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtCategoryComponentInOrder =
            itemView.findViewById<TextView>(R.id.txtCategoryComponentInOrder)
        var txtItemQuantityInCategoryComponent =
            itemView.findViewById<TextView>(R.id.txtItemQuantityInCategoryComponent)
        var viewRootCategoryInOrder =
            itemView.findViewById<LinearLayout>(R.id.viewRootCategoryInOrder)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_component_in_order, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCategory = listData[position]

        /** Set Text --> Category Name */
        holder.txtCategoryComponentInOrder.text = itemCategory.category_name

        /** Set Trang Thai cho Category khi được chọn*/
        if (itemCategory.category_id != chosenCategory) {
            holder.viewRootCategoryInOrder.background = context.getDrawable(R.drawable.bg_category_not_be_chosen)
        } else  {
            holder.viewRootCategoryInOrder.background = context.getDrawable(R.drawable.bg_txt_login)
        }
        /** getNumberItemInCategory in Order Fragment: With category_id AND TextView of ItemQuantityInCategory */
        getNumberItemInCategory(itemCategory.category_id, holder.txtItemQuantityInCategoryComponent)

        /** Click Category, with itemCategory.category_id --> Set change Status (color) */
        holder.viewRootCategoryInOrder.setOnClickListener {
            listenerClickCategoryInOrder.clickCategoryInOrder(itemCategory.category_id)
        }
    }


    private fun getNumberItemInCategory(categoryId: Int, txt: TextView) {
        DatabaseUtil.getListCategoryComponentItem(categoryId).observe(lifecycleOwner) {
            txt.text = it.size.toString() + " item(s)"
        }

    }

    /** Click Category. Choose and change Status (color) */
    @SuppressLint("NotifyDataSetChanged")
    fun setChooseCategory(chosenCategory: Int) {
        this.chosenCategory = chosenCategory
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<CategoryEntity>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    interface EventClickCategoryInOrderListener {
        fun clickCategoryInOrder(chooseCategory: Int)
    }
}