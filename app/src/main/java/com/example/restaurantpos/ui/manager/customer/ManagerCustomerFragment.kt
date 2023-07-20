package com.example.restaurantpos.ui.manager.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentManagerCustomerBinding
import com.example.restaurantpos.db.entity.CustomerEntity

class ManagerCustomerFragment : Fragment() {
    lateinit var binding: FragmentManagerCustomerBinding
    lateinit var adapterCustomer: CustomerAdapter

    lateinit var viewModelCustomer: CustomerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagerCustomerBinding.inflate(inflater, container, false)
        /** ViewModel */
        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Adapter: Tạo --> Set --> Push Data */
        adapterCustomer = CustomerAdapter(requireParentFragment(), ArrayList(), object :
            CustomerAdapter.EventClickItemCustomerListener {
            override fun clickCustomer(itemCustomer: CustomerEntity) {
                // Điều hướng sang xem ListOrderOfCustomer + send customerObject(key) &  (value)
                // itemCustomer.customer_id   <-- Là thứ mình cần sử dụng ở Fragment bên kia

                if (itemCustomer.customer_id == 0){
                    findNavController().navigate(
                        R.id.action_mainManagerFragment_to_customerOrderFragment,
                        bundleOf("customerObject" to "")
                    )
                }else{
                    findNavController().navigate(
                        R.id.action_mainManagerFragment_to_customerOrderFragment,
                        bundleOf("customerObject" to itemCustomer.toJson())
                    )
                }
            }
        })
        binding.rcyCustomerManagement.adapter = adapterCustomer
        viewModelCustomer.getListCustomer().observe(viewLifecycleOwner) { listCus ->
            adapterCustomer.setListData(listCus)
        }

        /** --------------------------------------------------------------------------- */

        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }
}