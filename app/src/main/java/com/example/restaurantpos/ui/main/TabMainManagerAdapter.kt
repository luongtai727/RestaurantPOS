package com.example.restaurantpos.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.restaurantpos.ui.manager.category.ManagerCategoryFragment
import com.example.restaurantpos.ui.manager.customer.ManagerCustomerFragment
import com.example.restaurantpos.ui.manager.home.ManagerHomeFragment
import com.example.restaurantpos.ui.manager.user.ManagerUserFragment

class TabMainManagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
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
            else -> {
                ManagerHomeFragment()
            }
        }
    }


}