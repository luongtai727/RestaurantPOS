package com.example.restaurantpos.ui.staff.receptionist.checkout.confirm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentCheckoutConfirmBinding
import com.example.restaurantpos.db.entity.BillEntity
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.ui.manager.category.CategoryViewModel
import com.example.restaurantpos.ui.manager.customer.CustomerViewModel
import com.example.restaurantpos.ui.staff.receptionist.checkout.ItemCheckoutAdapter
import com.example.restaurantpos.ui.staff.receptionist.order.CartViewModel
import com.example.restaurantpos.ui.staff.receptionist.order.CustomerInnerAdapter
import com.example.restaurantpos.ui.staff.receptionist.table.TableViewModel
import com.example.restaurantpos.util.DateFormatUtil
import java.util.Calendar

class CheckoutConfirmFragment : Fragment() {
    lateinit var binding: FragmentCheckoutConfirmBinding

    /** ViewModel Object */
    private lateinit var viewModelCart: CartViewModel
    private lateinit var viewModelItem: CategoryViewModel
    private lateinit var viewModelTable: TableViewModel
    private lateinit var viewModelCustomer: CustomerViewModel

    private lateinit var adapterItemCheckout: ItemCheckoutAdapter


    private lateinit var adapterCustomerInner: CustomerInnerAdapter

    // Tạo sẵn Object --> Xíu nữa hứng data get được. Từ database/fragment before
    private var tableObject: TableEntity? = null
    private var orderObject: OrderEntity? = null
    private var customerObject: CustomerEntity? = null
    private var billObject: BillEntity? = null


    // Dialog cho Customer
    lateinit var dialog: AlertDialog

    val calendar = Calendar.getInstance()


    private val tax = 0.1f
    var subTotal = 0.0f
    var billAmount = 0.0f
    var change = 0.0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutConfirmBinding.inflate(inflater, container, false)
        /** Create ViewModel Object */
        viewModelCart = ViewModelProvider(this).get(CartViewModel::class.java)
        viewModelItem = ViewModelProvider(this).get(CategoryViewModel::class.java)
        viewModelTable = ViewModelProvider(this).get(TableViewModel::class.java)
        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)

        return binding.root
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** ---------------------------------------------------------- */
        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        /** Code for Back */
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.txtBack.setOnClickListener {
            findNavController().popBackStack()
        }



        /** Adapter BILL */
        adapterItemCheckout = ItemCheckoutAdapter(requireContext(), ArrayList(), viewLifecycleOwner)
        binding.rcyItemInBill.adapter = adapterItemCheckout

        /** Xử lý đáp data từ fragment trước */
        // Cần Table --> Chuyển Table về trạng thái Empty
        // Cần Order --> Tính tiền cho Order đấy
        tableObject =
            TableEntity.toTableEntity(requireArguments().getString("tableObjectQ").toString())

        orderObject =
            OrderEntity.toOrderObject(requireArguments().getString("orderObjectQ").toString())

        billObject =
            BillEntity.toBill(requireArguments().getString("billObjectQ").toString())


        binding.txtDate.text = billObject?.bill_date
        binding.txtTable.text = billObject?.bill_table_name
        binding.txtCustomer.text = billObject?.bill_customer
        binding.txtStaff.text = billObject?.bill_staff
        binding.txtSubTotal.text = String.format("%.1f", billObject?.bill_subTotal)
        binding.txtCoupon.text = billObject?.bill_coupon.toString()
        binding.txtCustomerRank.text = billObject?.bill_customer_rank_percent.toString()
        binding.txtTax.text = billObject?.bill_tax.toString()
        binding.txtTotal.text = String.format("%.1f", billObject?.bill_total)
        binding.txtCash.text = billObject?.bill_cash
        binding.txtChange.text = billObject?.bill_change

        /** ----------------------------------------------------------------------------------*/
        // Map(Key, Value)
        // Key: Item_id
        // Value: CartItem (Object)

        tableObject?.let { table ->
//            binding.txtTable.text = table.table_name
            viewModelCart.getListCartItemByTableIdAndOrderStatus(table.table_id)
                .observe(viewLifecycleOwner) { listCart ->

                    val mergedMap = mutableMapOf<Int, CartItemEntity>()

                    // Gộp các phần tử trùng nhau và tính tổng số lượng
                    for (cartItem in listCart) {
                        if (mergedMap.containsKey(cartItem.item_id)) {
                            val existingItem = mergedMap[cartItem.item_id]!!
                            existingItem.order_quantity += cartItem.order_quantity
                        } else {
                            // Nếu phần tử chưa tồn tại trong mergedMap, thêm cartItem vào mergedMap dựa trên item_id.
                            mergedMap[cartItem.item_id] = cartItem
                        }
                    }
                    // Chuyển đổi map thành list đã gộp
                    val mergedList = ArrayList(mergedMap.values)

                    adapterItemCheckout.setListData(mergedList)
                }
        }


        /** Code for Done */
        binding.txtDone.setOnClickListener {

            // Chốt Order này
            orderObject?.order_status_id = 2
            orderObject?.bill_total = billObject!!.bill_total
            orderObject?.paid_time = DateFormatUtil.getTimeForOrderCreateTime()
            orderObject?.let {
                viewModelCart.addOrder(it)
            }

            // Set lại Table is Empty and update Status on Database
            tableObject?.table_status_id = 0
            tableObject?.let { tableObject ->
                viewModelTable.addTable(requireContext(), tableObject)
            }

            findNavController().navigate(R.id.action_checkoutConfirmFragment_to_checkoutDoneFragment)
        }


        /** ----------------------------------------------------------------------------------*/
        /** Handle Checkout */
        // Total = subTotal - subTotal*coupon + subTotal*Tax
        // Change = Total - Cash
        /** subTotal */


        /** ---------------------------------------------------------- */
        /** 2. COUPON --> BILL AMOUNT */
        // 1. Nhấp vào Apply Coupon --> Hiện ra để nhập
        // 2. Đối chiếu xem đúng mã hay không --> Đúng thì apply thành công, sai thì hiện thông báo fail
        // 3. Cancel thì trả lại trạng thái ban đầu


        /** 3. CASH --> CHANGE */


        /** Code for CHECK OUT */


        /** ----------------------------------------------------------------------------------*/


        /** Code for Customer TextView */


        /** ----------------------------------------------------------*/
        /** Add Customer Dialog */

    }
}