package com.example.restaurantpos.ui.manager.customer.order

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
import com.example.restaurantpos.databinding.FragmentCustomerOrderBinding
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.ui.manager.customer.CustomerViewModel
import com.example.restaurantpos.ui.staff.receptionist.order.CartViewModel

class CustomerOrderFragment : Fragment() {

    lateinit var binding: FragmentCustomerOrderBinding
    lateinit var adapterCustomerOrder: CustomerOrderAdapter

    lateinit var viewModelCustomer: CustomerViewModel
    lateinit var viewModelCart: CartViewModel

    // Lấy customer_id --> Get ra List Order
    // Nếu Null--> Default UnknownCustomer
    // Send data sang Bill of Order of Customer cũng cần đến
    lateinit var customerObject: CustomerEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCustomerOrderBinding.inflate(inflater, container, false)
        /** ViewModel */
        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCart = ViewModelProvider(this).get(CartViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Nếu Null--> Default UnknownCustomer
        customerObject = CustomerEntity(0, "Unkown", "...", "...", 0.0)
        // Còn không--> Lấy Customer từ Fragment trước đáp sang
        if (requireArguments().getString("customerObject").toString() != "") {
            customerObject = requireArguments().getString("customerObject")?.let {
                CustomerEntity.toCustomerEntity(
                    it
                )
            }!!
        }


        /** Adapter: Tạo --> Set --> Push Data */
        adapterCustomerOrder =
            CustomerOrderAdapter(requireParentFragment(), ArrayList(), object :
                CustomerOrderAdapter.EventClickCustomerDetailByOrderListener {
                override fun clickOrderofCustomer(order: OrderEntity) {
                    //  Điều hướng sang xem Bill của Order đấy. Gửi luôn data của OrderEntity sang.
                    findNavController().navigate(
                        R.id.action_customerOrderFragment_to_customerOrderBillFragment,
                        bundleOf("orderObject" to order.toJson())
                    )
                }
            })
        binding.rcyCustomerManagement.adapter = adapterCustomerOrder
        // Làm sao có customer_id
        viewModelCart.getListOrderByCustomerId(customerObject.customer_id)
            .observe(viewLifecycleOwner) { listOrderOfCustomer ->
                adapterCustomerOrder.setListData(listOrderOfCustomer)
            }

        /**-------------------------------------------------------------------------*/
        /**Customer Information on Top*/
        binding.txtCustomerName.text = customerObject.customer_name
        binding.txtCustomerPhone.text = customerObject.phone
        binding.txtCustomerBirthday.text = customerObject.birthday

        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        /** imgBack */
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}