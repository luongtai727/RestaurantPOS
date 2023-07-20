package com.example.restaurantpos.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.ui.manager.category.ManagerCategoryFragment
import com.example.restaurantpos.ui.manager.customer.ManagerCustomerFragment
import com.example.restaurantpos.ui.manager.home.ManagerHomeFragment
import com.example.restaurantpos.ui.manager.user.ManagerUserFragment

/*
class CategoryAdapter(private val myContext: Context, fm: FragmentManager, private var totalTabs : Int) : FragmentPagerAdapter(fm) {
// private var totalTabs : Int   --> Truyền vào số lượng

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> AFragment(position)
            1 -> BFragment()
            2,3,4,5,6,7,8 -> TabMemeFragment(position-2)
            else -> TapProfFragment(position-9)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}*/
