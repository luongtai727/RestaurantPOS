package com.example.restaurantpos.ui.manager.coupon

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.CouponEntity

class CouponAdapter(
    var context: Context,
    private var listData: MutableList<CouponEntity>,
    val listenerClickCoupon: EventClickCouponListener
) : RecyclerView.Adapter<CouponAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewRootCoupon = itemView.findViewById<LinearLayout>(R.id.viewRootCoupon)
        var txtNo = itemView.findViewById<TextView>(R.id.txtNo)
        var txtDate = itemView.findViewById<TextView>(R.id.txtDate)
        var txtCouponCode = itemView.findViewById<TextView>(R.id.txtCouponCode)
        var txtDiscount = itemView.findViewById<TextView>(R.id.txtDiscount)
        var txtCouponStatus = itemView.findViewById<TextView>(R.id.txtCouponStatus)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coupon, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    // Position ở đây sẽ ứng dụng position của List trong Database
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val couponItem = listData[position]
        /** Đổ data lên View */
        holder.txtNo.text = "${position + 1}"
        holder.txtDate.text = couponItem.coupon_add_date
        holder.txtCouponCode.text = couponItem.coupon_code
        holder.txtDiscount.text = couponItem.coupon_discount.toString()
        when (couponItem.coupon_status) {
            0 -> {
                holder.txtCouponStatus.text = "Inactive"
                holder.txtCouponStatus.setBackgroundResource(R.drawable.bg_status_gray)
            }

            1 -> {
                holder.txtCouponStatus.text = "Active"
                holder.txtCouponStatus.setBackgroundResource(R.drawable.bg_status_green)
            }

            else -> {
                holder.txtCouponStatus.text = "Active"
                holder.txtCouponStatus.setBackgroundResource(R.drawable.bg_status_green)
            }
        }

        holder.viewRootCoupon.setOnLongClickListener {
            listenerClickCoupon.clickCoupon(couponItem)
            true
        }

        holder.txtCouponStatus.setOnClickListener {
            listenerClickCoupon.clickCouponStatus(couponItem)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<CouponEntity>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }


    interface EventClickCouponListener {
        fun clickCoupon(coupon: CouponEntity)
        fun clickCouponStatus(coupon: CouponEntity)
    }
}
