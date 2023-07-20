package com.example.restaurantpos.ui.manager.user

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.db.entity.AccountEntity

class ManagerUserAdapter(
    var context: Context,
    private var listData: MutableList<AccountEntity>,
    val listenerClickEditUser: EventClickItemUserListener
) : RecyclerView.Adapter<ManagerUserAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtAccountName = itemView.findViewById<TextView>(R.id.txtUserName)
        var txtUserRole = itemView.findViewById<TextView>(R.id.txtUserRole)
        var imgEditUser = itemView.findViewById<ImageView>(R.id.imgEditUser)
        var imgLock = itemView.findViewById<ImageView>(R.id.imgLock)
    }

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(convertedView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUser = listData[position]

        holder.txtAccountName.text = itemUser.account_name
        holder.txtUserRole.text = if (itemUser.role_id == 1) {
            "Receptionist"
        } else {
            "Kitchen"
        }

        holder.imgLock.visibility = if (!itemUser.account_status_id){
            View.VISIBLE
        }  else {
            View.INVISIBLE
        }

        holder.imgEditUser.setOnClickListener {
            listenerClickEditUser.clickEditUser(itemUser)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(newListData: MutableList<AccountEntity>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    interface EventClickItemUserListener {
        fun clickEditUser(itemUser: AccountEntity)
    }
}