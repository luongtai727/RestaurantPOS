package com.example.restaurantpos.ui.manager.category

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.restaurantpos.db.entity.CategoryEntity

class TabCategoryAdapter(fm: FragmentManager, var totalTabs : MutableList<CategoryEntity>) : FragmentPagerAdapter(fm) {

    // Với mỗi loại Category mình chỉ có 1 thằng Type thôi, nên thực ra chỉ khác mỗi posirion
    // --> Tạo 1 frament chung sau đó truyền trọng số vị trí vào là được.
    // --> TypeOfCategoryFragment
    override fun getItem(position: Int): Fragment {
        return ManagerCategoryComponentFragment(position, totalTabs[position])
    }

    // Lầy từ DB, sau này có thể thay đổi Category
    // Không như Bottom Navigation ở màn chính. Biết = 4 rồi
    override fun getCount(): Int {
        return totalTabs.size
    }
}