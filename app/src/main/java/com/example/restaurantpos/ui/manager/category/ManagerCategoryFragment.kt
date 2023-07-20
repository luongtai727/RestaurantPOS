package com.example.restaurantpos.ui.manager.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.restaurantpos.databinding.FragmentManagerCategoryBinding
import com.example.restaurantpos.db.entity.CategoryEntity
import com.google.android.material.tabs.TabLayout

class ManagerCategoryFragment : Fragment() {

    lateinit var binding: FragmentManagerCategoryBinding
    private lateinit var viewModel: CategoryViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagerCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        viewModel.getAllCategory().observe(viewLifecycleOwner) { listCategory ->
            binding.tabCategory.removeAllTabs()
            listCategory.forEach { categoryItem ->
                binding.tabCategory.addTab(
                    binding.tabCategory.newTab().setText(categoryItem.category_name)
                )
            }
            // Đặt adapter đây để chống Null. <-- Đảm bảo phải get đưuọc getAllCategory() mới chạy
            val categoryAdapter = TabCategoryAdapter(
                childFragmentManager,
                listCategory
            )
            binding.vpgCategory.adapter = categoryAdapter

            // Xử lý TabClick vs swipeTheScreen (TapLayout vs ViewPager)
            binding.vpgCategory.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabCategory))
            binding.tabCategory.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.vpgCategory.currentItem = tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })

            //
        }
    }
}