package com.example.restaurantpos.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentMainManagerBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainManagerFragment : Fragment() {

    lateinit var binding: FragmentMainManagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentContainerView.adapter = TabMainManagerAdapter(requireParentFragment())
        requireParentFragment()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    binding.fragmentContainerView.currentItem = 0
                }

                R.id.menu_users -> {
                    binding.fragmentContainerView.currentItem = 1
                }

                R.id.menu_category -> {
                    binding.fragmentContainerView.currentItem = 2
                }

                R.id.menu_customers -> {
                    binding.fragmentContainerView.currentItem = 3
                }
            }

            true
        })
    }
}