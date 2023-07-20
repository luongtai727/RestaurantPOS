package com.example.restaurantpos.ui.manager.user

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.restaurantpos.R

class UserRoleSpinnerAdapter(
    val activity: Activity,
    val listUserRole: List<String>
) : ArrayAdapter<String>(activity, R.layout.user_role_spinner) {

    //1. So luong hien thi len Spinner
    override fun getCount(): Int {
        return listUserRole.size
    }

    //2. Khi users chua chon gi thi getView() se hien thi
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return handleView(position, convertView, parent)
    }

    //3. Khi users click vao Spinner va bat dau Select
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return handleView(position, convertView, parent)
    }

    private fun handleView(position: Int, convertView: View?, parent: ViewGroup): View {
        val context = activity.layoutInflater
        // layoutInflater ==> La 1 component ==> Chuyen doi layout XML --> View trong android
        val inRowView = context.inflate(R.layout.user_role_spinner, parent, false)

        // Khai bao bien de lien ket UserRole. Lay resource tu list_monan.XML
        val txtEachUserRole = inRowView.findViewById<TextView>(R.id.txtEachUserRole)

        txtEachUserRole.text = listUserRole[position]
        return inRowView
    }
}