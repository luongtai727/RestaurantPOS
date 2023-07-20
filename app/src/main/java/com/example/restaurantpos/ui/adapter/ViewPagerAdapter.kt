package com.example.restaurantpos.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.restaurantpos.ui.manager.category.ManagerCategoryFragment
import com.example.restaurantpos.ui.manager.customer.ManagerCustomerFragment
import com.example.restaurantpos.ui.manager.home.ManagerHomeFragment
import com.example.restaurantpos.ui.manager.user.ManagerUserFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {


        override fun getCount(): Int {
            return 4
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ManagerHomeFragment()
                }

                1 -> {
                    ManagerUserFragment()
                }

                2 -> {
                    ManagerCategoryFragment()
                }

                3 -> {
                    ManagerCustomerFragment()
                }
                // Tất cả trường hợp lỗi --> Khởi tạo Home trở lại
                else -> {
                    ManagerHomeFragment()
                }
            }

        }


}