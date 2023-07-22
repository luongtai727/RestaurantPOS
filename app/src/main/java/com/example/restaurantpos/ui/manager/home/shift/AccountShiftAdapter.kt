package com.example.restaurantpos.ui.manager.home.shift

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountShift
import com.example.restaurantpos.db.entity.AccountShiftEntity

class AccountShiftAdapter(
    var accounts: List<AccountShift>,
    val listenerClick: AccountShiftAdapter.EventClickListener

) : RecyclerView.Adapter<AccountShiftAdapter.AccountViewHolder>() {

    class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtName = itemView.findViewById<TextView>(R.id.tv_dialog_name)
        var imgDelete = itemView.findViewById<ImageView>(R.id.img_delete_account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account_shift, parent, false)
        return AccountViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
       return accounts.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accounts.get(position)

        holder.txtName.text = account.name
        holder.imgDelete.setOnClickListener {
            listenerClick.onClickDelete(account.account_shift_id)
        }
    }

    interface EventClickListener {
        fun onClickDelete(shift_id: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<AccountShift>) {
        accounts = newListData
        notifyDataSetChanged()
    }
}