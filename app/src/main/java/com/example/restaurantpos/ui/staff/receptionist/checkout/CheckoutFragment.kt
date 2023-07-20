package com.example.restaurantpos.ui.staff.receptionist.checkout

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.databinding.FragmentCheckoutBinding
import com.example.restaurantpos.db.entity.BillEntity
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.db.entity.CouponEntity
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.ui.manager.category.CategoryViewModel
import com.example.restaurantpos.ui.manager.coupon.CouponViewModel
import com.example.restaurantpos.ui.manager.customer.CustomerViewModel
import com.example.restaurantpos.ui.staff.receptionist.order.CartViewModel
import com.example.restaurantpos.ui.staff.receptionist.order.CustomerInnerAdapter
import com.example.restaurantpos.ui.staff.receptionist.table.TableViewModel
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.DatabaseUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.gone
import com.example.restaurantpos.util.hide
import com.example.restaurantpos.util.show
import com.example.restaurantpos.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding

    /** ViewModel Object */
    private lateinit var viewModelCart: CartViewModel
    private lateinit var viewModelItem: CategoryViewModel
    private lateinit var viewModelTable: TableViewModel
    private lateinit var viewModelCustomer: CustomerViewModel
    private lateinit var viewModelCoupon: CouponViewModel


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

    var discount = 0
    var couponDiscount = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        /** Create ViewModel Object */
        viewModelCart = ViewModelProvider(this).get(CartViewModel::class.java)
        viewModelItem = ViewModelProvider(this).get(CategoryViewModel::class.java)
        viewModelTable = ViewModelProvider(this).get(TableViewModel::class.java)
        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCoupon = ViewModelProvider(this).get(CouponViewModel::class.java)


        setChange(binding.edtCash.text.toString())

        return binding.root
    }


    @SuppressLint("ResourceAsColor")
    private fun calculateDiscountPercentage(listOrderOfCustomer: MutableList<OrderEntity>) {
        val sum = listOrderOfCustomer.sumOf { it -> it.bill_total.toLong() }

        val color0 =
            ContextCompat.getColor(requireContext(), com.example.restaurantpos.R.color.text_rank_0)
        val colorStateList0 = ColorStateList.valueOf(color0)

        val color1 =
            ContextCompat.getColor(requireContext(), com.example.restaurantpos.R.color.text_rank_1)
        val colorStateList1 = ColorStateList.valueOf(color1)

        val color2 =
            ContextCompat.getColor(requireContext(), com.example.restaurantpos.R.color.text_rank_2)
        val colorStateList2 = ColorStateList.valueOf(color2)

        val color3 =
            ContextCompat.getColor(requireContext(), com.example.restaurantpos.R.color.text_rank_3)
        val colorStateList3 = ColorStateList.valueOf(color3)

        if (sum >= 2000) {
            discount = 5
            binding.imgRank.imageTintList = colorStateList1
            binding.txtDiscountOnRank.setTextColor(com.example.restaurantpos.R.color.text_rank_1)
        } else if (sum >= 5000) {
            discount = 10
            binding.imgRank.imageTintList = colorStateList2
            binding.txtDiscountOnRank.setTextColor(com.example.restaurantpos.R.color.text_rank_2)
        } else if (sum >= 10000) {
            discount = 15
            binding.imgRank.imageTintList = colorStateList3
            binding.txtDiscountOnRank.setTextColor(com.example.restaurantpos.R.color.text_rank_3)
        } else {
            discount = 0
            binding.imgRank.imageTintList = colorStateList0
            binding.txtDiscountOnRank.setTextColor(com.example.restaurantpos.R.color.text_rank_0)
        }

        binding.txtDiscountOnRank.text = discount.toString().plus("%")
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Adapter BILL */
        adapterItemCheckout = ItemCheckoutAdapter(requireContext(), ArrayList(), viewLifecycleOwner)
        binding.rcyItemInBill.adapter = adapterItemCheckout

        /** Xử lý đáp data từ fragment trước */
        // 1. tableObject lấy từ ............... This <-- OldOrderFragment <-- TableFragment.
        // <--- bundleOf("tableObject" to itemTable.toJson())   <--  override fun clickTable(itemTable: TableEntity, table_status: Int)

        // Cần Table --> Chuyển Table về trạng thái Empty
        // Cần Order --> Tính tiền cho Order đấy
        tableObject =
            TableEntity.toTableEntity(requireArguments().getString("tableObject").toString())

        // 2. orderObject lấy từ ...............This <-- OldOrderFragment <-- TableFragment.
        // <--- bundleOf("tableObject" to itemTable.toJson())   <--  tableObject?.let { table ->
        // viewModelCart.getOrderByTable(table.table_id).observe(viewLifecycleOwner) { order -> orderObject = order }
        orderObject =
            OrderEntity.toOrderObject(requireArguments().getString("orderObject").toString())

        /** Handle Customer */
        /*        viewModelCustomer.getListCustomer()
                    .observe(viewLifecycleOwner) { listCustomer ->
                        if (listCustomer.isNotEmpty()) {
                            val customerObject = listCustomer.stream()
                                .filter { it -> it.customer_id == orderObject?.customer_id }.collect(
                                Collectors.toList()
                            ).get(0)
                            binding.txtCustomerInBill.text = customerObject.customer_name
                        }
                    }*/

        // Qnew
        viewModelCart.getListOrderByCustomerId(orderObject!!.customer_id)
            .observe(viewLifecycleOwner) { listOrderOfCustomer ->
                calculateDiscountPercentage(listOrderOfCustomer)
            }
        /** ----------------------------------------------------------------------------------*/
        // Map(Key, Value)
        // Key: Item_id
        // Value: CartItem (Object)

        tableObject?.let { table ->
            binding.txtTableName.text = table.table_name
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

        /** ----------------------------------------------------------------------------------*/
        /** Handle Checkout */
        // Total = subTotal - subTotal*coupon + subTotal*Tax
        // Change = Total - Cash
        /** subTotal */
        CoroutineScope(Dispatchers.IO).launch {
            subTotal = DatabaseUtil.getSubTotal(orderObject!!.order_id)
            binding.txtSubTotal.text = String.format("%.1f", subTotal)

            billAmount = (subTotal * (1.0f + tax))
            binding.txtBillAmount.text = String.format("%.1f", billAmount)

//            binding.txtChange.text = "0.0"
        }


        /** ---------------------------------------------------------- */
        /** 2. COUPON --> BILL AMOUNT */
        // 1. Nhấp vào Apply Coupon --> Hiện ra để nhập
        // 2. GetCoupon ra để đối chiếu nữa
        // 3. Đối chiếu xem đúng mã hay không --> Đúng thì apply thành công, sai thì hiện thông báo fail
        // 4. Cancel thì trả lại trạng thái ban đầu


        binding.txtAddCoupon.setOnClickListener {
            if (binding.llCoupon.visibility == View.VISIBLE) {
                binding.llCoupon.visibility = View.GONE
                binding.txtAddCoupon.text = "Apply Coupon?"
                binding.txtAddCoupon.show()
            } else {
                binding.llCoupon.visibility = View.VISIBLE
                binding.txtAddCoupon.text = ""
                binding.txtAddCoupon.show()
            }
        }


        DataUtil.setEditTextWithoutSpecialCharactersAndSpaces(
            binding.edtCoupon,
            binding.txtAddCoupon
        )
        binding.txtAddCoupon.show()




        binding.txtApplyCoupon.setOnClickListener {
            binding.edtCoupon.clearFocus()
            binding.txtAddCoupon.hide()

            viewModelCoupon.couponGetByCouponCode.value = mutableListOf<CouponEntity>()
            viewModelCoupon.getCouponByCouponCode(binding.edtCoupon.text.toString().trim())

            viewModelCoupon.couponGetByCouponCode.observe(viewLifecycleOwner){ couponGetByCouponCode->

                // Nếu nhập mã 4~10 kí tự
                if (binding.edtCoupon.text.length >= 4) {
                    // Nếu tồn tại Coupon nhập vào
                    if (!couponGetByCouponCode.isNullOrEmpty()) {
                        Log.d("Quanglt", "$couponGetByCouponCode")
                        // Nếu Coupon đấy còn hiêu lực
                        if (binding.edtCoupon.text.toString() == couponGetByCouponCode[0].coupon_code && couponGetByCouponCode[0].coupon_status == 1) {

                            billAmount =
                                (subTotal * (1 - (couponGetByCouponCode[0].coupon_discount) / 100.0) * (1 + tax)).toFloat()
                            binding.txtBillAmount.text = String.format("%.1f", billAmount)
                            binding.txtAddCoupon.text =
                                "Coupon was applied successfully! - ${couponGetByCouponCode[0].coupon_discount}%"
                            binding.txtAddCoupon.show()
                            couponDiscount = couponGetByCouponCode[0].coupon_discount
                            binding.txtAddCoupon.setTextColor(com.example.restaurantpos.R.color.money)

                        } else if (couponGetByCouponCode[0].coupon_status != 1) {
                            binding.edtCoupon.clearFocus()
                            binding.txtAddCoupon.text = ""
                            binding.txtAddCoupon.text = "This coupon has expired."
                            binding.txtAddCoupon.show()
                            binding.txtAddCoupon.setTextColor(com.example.restaurantpos.R.color.text_rank_1)
                        }
                    } else {
                        binding.edtCoupon.clearFocus()
                        binding.txtAddCoupon.text = "This coupon is not valid!"
                        binding.txtAddCoupon.show()
                        binding.txtAddCoupon.setTextColor(com.example.restaurantpos.R.color.text_red)
                    }
                } else {
                    binding.txtAddCoupon.text =
                        "The Coupon Code needs to consist of 4 to 8 characters!"
                    binding.txtAddCoupon.show()
                }
            }
        }

        binding.txtCancelCoupon.setOnClickListener {
            binding.llCoupon.gone()
            binding.txtAddCoupon.text = "Apply Coupon?"
            binding.txtAddCoupon.show()
            billAmount = (subTotal * (1.0f + tax))
            binding.txtBillAmount.text = String.format("%.1f", billAmount)
        }

        /** 3. CASH --> CHANGE */
        binding.edtCash.doOnTextChanged { text, _, _, _ ->

            setChange(text)

        }

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


        /** Code for CHECK OUT */
        binding.txtCheckout.setOnClickListener {
            if (binding.edtCash.text.isNotEmpty()) {
                if (binding.txtChange.text != "0.0" && (binding.edtCash.text.toString()
                        .toFloat() > billAmount)
                ) {
                    /** Put data for billObject  -----> BillFragment */
                    billObject = BillEntity(
                        orderObject!!.order_id,
                        tableObject!!.table_name,
                        binding.txtCustomerInBill.text.toString(),
                        SharedPreferencesUtils.getAccountName(),
                        subTotal,
                        couponDiscount,
                        discount,
                        tax * 100,
                        billAmount,
                        binding.edtCash.text.toString().trim(),
                        binding.txtChange.text.toString()
                    )
                    findNavController().navigate(
                        com.example.restaurantpos.R.id.action_checkoutFragment_to_checkoutConfirmFragment,
                        bundleOf(
                            "tableObjectQ" to tableObject?.toJson(),
                            "orderObjectQ" to orderObject?.toJson(),
                            "billObjectQ" to billObject?.toJson()
                        )
                    )
                } else {
                    binding.txtError.show()
                }
            } else {
                binding.txtError.show()
            }

        }


        /** ----------------------------------------------------------------------------------*/


        /** Code for Customer TextView */
        binding.txtCustomerInBill.setOnClickListener {
            showDialogCustomer()
        }
    }

    // 再利用コード    Võ chỉ cho
    private fun setChange(text: CharSequence?) {
        if (text.toString().isNotEmpty()) {
            val cash = text.toString().toFloat()
            if (cash > billAmount) {
                change = cash - billAmount
                binding.txtChange.text = String.format("%.1f", change)
            } else {
                binding.txtChange.text = "0.0"
            }
        } else {
            binding.txtChange.text = "0.0"
        }
    }

    /** ----------------------------------------------------------*/
    /** Add Customer Dialog */

    val startYear = calendar.get(Calendar.YEAR) - 20
    val startMonth = calendar.get(Calendar.MONTH) - 5
    val startDay = calendar.get(Calendar.DAY_OF_MONTH) - 10

    @SuppressLint("SetTextI18n")
    private fun showDialogCustomer() {
        // -----------------Prepare--------------------------------------------------//
        // 1.  Build Dialog
        // 2.  Designed XML --> View
        // 3.  Set VIEW tra ve above --> Dialog
        val build =
            AlertDialog.Builder(
                requireActivity(),
                com.example.restaurantpos.R.style.ThemeCustom
            )
        val view = layoutInflater.inflate(
            com.example.restaurantpos.R.layout.dialog_alert_add_customer,
            null
        )
        build.setView(view)
        // 4.  Get Component of Dialog
        val edtPhoneNumber =
            view.findViewById<EditText>(com.example.restaurantpos.R.id.edtPhoneNumber)
        val rcyCustomerInPhone =
            view.findViewById<RecyclerView>(com.example.restaurantpos.R.id.rcyCustomerInPhone)
        val edtCustomerName =
            view.findViewById<EditText>(com.example.restaurantpos.R.id.edtCustomerName)
        val txtCustomerBirthday =
            view.findViewById<TextView>(com.example.restaurantpos.R.id.txtCustomerBirthday)
        val btnAddCustomer =
            view.findViewById<Button>(com.example.restaurantpos.R.id.btnAddCustomer)
        val btnCancel = view.findViewById<Button>(com.example.restaurantpos.R.id.btnCancel)
        val imgDate = view.findViewById<ImageView>(com.example.restaurantpos.R.id.imgDate)
        val imgCloseDialogCustomer =
            view.findViewById<ImageView>(com.example.restaurantpos.R.id.imgCloseDialogCustomer)
        // -----------------Code for Component----------------------------------------//
        // 1.  Handle Adapter CustomerPhone + Code of clickCustomerInner (Get CustomerInfo and set to View in Order)
        adapterCustomerInner =
            CustomerInnerAdapter(requireParentFragment(), ArrayList(), object :
                CustomerInnerAdapter.EventClickItemCustomerInnerListener {
                override fun clickCustomerInner(itemCustomer: CustomerEntity) {
                    // Có sẵn thì pick-up ra thôi
                    customerObject = itemCustomer

                    binding.txtCustomerInBill.text = itemCustomer.customer_name
                    dialog.dismiss()
                }
            })
        rcyCustomerInPhone.adapter = adapterCustomerInner

        // 2. Code for when staff types on edtPhoneNumber and contain >= 3 Chars. If exist --> Show for Picking-up
        // SetData for (1)
        edtPhoneNumber.doOnTextChanged { text, start, before, count ->
            if (text.toString().length >= 3) {
                viewModelCustomer.getListCustomerByPhoneForSearch(text.toString())
                    .observe(viewLifecycleOwner) {
                        if (it.size > 0) {
                            adapterCustomerInner.setListData(it as ArrayList<CustomerEntity>)
                            rcyCustomerInPhone.show()
                        }
                    }
            } else {
                rcyCustomerInPhone.gone()
            }
        }

        // 3. Birthday
        imgDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    txtCustomerBirthday.text = "$year/${1 + month}/$dayOfMonth"
                },
                startYear, startMonth, startDay
            ).show()
        }

        // 4.  Add Customer
        btnAddCustomer.setOnClickListener {
            if (edtCustomerName.text.isEmpty() ||
                edtPhoneNumber.text.isEmpty() ||
                txtCustomerBirthday.text.isEmpty()
            ) {
                context?.showToast("Information must not be empty!")
            } else {
                viewModelCustomer.addCustomer(
                    CustomerEntity(
                        0,
                        edtCustomerName.text.toString(),
                        edtPhoneNumber.text.toString(),
                        txtCustomerBirthday.text.toString(),
                        0.0
                    )
                )
            }


            viewModelCustomer.getListCustomerByPhoneForAdd(edtPhoneNumber.text.toString())
                .observe(viewLifecycleOwner) { listCustomer ->
                    if (listCustomer.size > 0) {
                        customerObject = listCustomer[0]
                        binding.txtCustomerInBill.text = listCustomer[0].customer_name
                        dialog.dismiss()
                    }
                }
        }


        // Other:  Dau X  &   Cancel Button
        imgCloseDialogCustomer.setOnClickListener { dialog.dismiss() }
        btnCancel.setOnClickListener { dialog.dismiss() }

        // End: Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }
}