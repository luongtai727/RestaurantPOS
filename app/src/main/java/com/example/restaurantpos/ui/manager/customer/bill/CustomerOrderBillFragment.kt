package com.example.restaurantpos.ui.manager.customer.bill

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.databinding.FragmentCustomerOrderBillBinding
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.ui.manager.customer.CustomerViewModel
import com.example.restaurantpos.ui.staff.receptionist.order.CartViewModel


class CustomerOrderBillFragment : Fragment() {

    lateinit var binding: FragmentCustomerOrderBillBinding
    lateinit var adapterListCartItemOfCustomerOrderBill: ListCartItemOfCustomerOrderBillAdapter

    lateinit var viewModelCustomer: CustomerViewModel
    lateinit var viewModelCart: CartViewModel

    // Lấy customer_id --> Get ra List Order
    // Nếu Null--> Default UnknownCustomer
    // Send data sang Bill of Order of Customer cũng cần đến
//    lateinit var customerObject: CustomerEntity
    var orderObject: OrderEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerOrderBillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**Customer Information on Top*/
//        binding.txtCustomerName.text = customerObject.customer_name
//        binding.txtCustomerPhone.text = customerObject.phone
//        binding.txtCustomerBirthday.text = customerObject.birthday

        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        /** imgBack */
//        binding.imgBack.setOnClickListener {
//            findNavController().popBackStack()
//        }
        /**-------------------------------------------------------------------------*/
        /** ViewModel */
        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCart = ViewModelProvider(this).get(CartViewModel::class.java)

        // Lấy orderObject từ Fragment trước đáp sang
        if (requireArguments().getString("orderObject").toString() != "") {
            orderObject =
                OrderEntity.toOrderObject(requireArguments().getString("orderObject").toString())
        } else {
            findNavController().popBackStack()
        }


        /** Adapter: Tạo --> Set --> Push Data */
        adapterListCartItemOfCustomerOrderBill =
            ListCartItemOfCustomerOrderBillAdapter(
                requireContext(),
                viewLifecycleOwner,
                ArrayList()
            )
        binding.rcyCartItemOnBill.adapter = adapterListCartItemOfCustomerOrderBill
        Log.d("Quanglt", "$orderObject")
        // Làm sao có customer_id
        orderObject?.order_id?.let {
            viewModelCart.getListCartItemByOrderId(it)
                .observe(viewLifecycleOwner) { listCartItemOnBill ->
                    adapterListCartItemOfCustomerOrderBill.setListData(listCartItemOnBill)
                    Log.d("Quanglt", "$it")
                    Log.d("Quanglt", "$listCartItemOnBill")

//                    showAmountOfMoney(listCartItemOnBill as ArrayList<CartItemEntity>)

                }
        }
    }

    /**-------------------------------------------------------------------------*/
    private fun showAmountOfMoney() {
        //Tạo UI rồi Show tien bằng cách call :      adapter.getTien()


        /* Adapter: ListCartItemOfCustomerOrderBillAdapter
        * Mình có thể tái tạo sử dụng lại thằng này vì:
        * Khi mà nó được setData thì.
        * Sau khi clear thì nó sẽ bằng 0 theo
        * showInfo bây giờ cần cả số lượng vào nữa
        * Chú ý: BẢN THÂN THẰNG ADAPTER VẪN CÓ THỂ THỰC HIỆN TÍNH ĐƯỢC NHA
        * adapter.itemCount  --> Chú ý tận dụng hàm này nhé. Chính là hàm getItemCount() phía adapter
        *
        * */
//        val cartItemInList = 0.0
//        listCartItemOnBill.forEach {
//            cartItemInList += cartItemInList.price
//        }

        // Còn nếu vẫn muốn giữ nguyên cách tính như trên thì
        //
    }
}