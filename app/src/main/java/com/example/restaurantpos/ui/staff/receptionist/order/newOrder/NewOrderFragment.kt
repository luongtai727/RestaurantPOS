package com.example.restaurantpos.ui.staff.receptionist.order.newOrder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentNewOrderBinding
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.ui.manager.category.CategoryViewModel
import com.example.restaurantpos.ui.manager.customer.CustomerViewModel
import com.example.restaurantpos.ui.staff.receptionist.order.CartViewModel
import com.example.restaurantpos.ui.staff.receptionist.order.CustomerInnerAdapter
import com.example.restaurantpos.ui.staff.receptionist.table.TableViewModel
import com.example.restaurantpos.util.DateFormatUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.gone
import com.example.restaurantpos.util.show
import com.example.restaurantpos.util.showToast
import java.util.Calendar
import java.util.stream.Collectors

class NewOrderFragment : Fragment() {

    lateinit var binding: FragmentNewOrderBinding

    /** Adapter */
    lateinit var adapterCategoryInBottomOfOrderFragment: CategoryInBottomOfOrderFragmentAdapter
    lateinit var adapterOrderItem: ItemOfCategoryInBottomOfOrderFragmentAdapter
    lateinit var adapterCartItem: CartItemAdapter
    lateinit var adapterCustomerInner: CustomerInnerAdapter


    /** Những ViewModel chứa các phương thức cần sử dụng */
    lateinit var viewModelCategory: CategoryViewModel
    lateinit var viewModelCart: CartViewModel
    lateinit var viewModelTable: TableViewModel
    lateinit var viewModelCustomer: CustomerViewModel


    /** Tạo những đối tượng của Entity để dễ thao tác */
    private var tableObject: TableEntity? = null
    private var orderObject: OrderEntity? = null
    private var customerObject: CustomerEntity? = null
    private var listCartItem = ArrayList<CartItemEntity>()
    private var listItemOfCategory = ArrayList<ItemEntity>()

    // Để quản lý customer
    var idCustomer: Long = 0L

    // Để so sánh với category_id
    var chooseCategory: Int = 1

    // Dialog cho Customer
    lateinit var dialog: AlertDialog

    val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false)
        /** ----------------------------------------------------------------------------*/
        /** Xử lý Biến tableObject (data từ fragment trước) */
        // Chuyển TableEntity String thành 1 đối tượng để chuyển dữ liệu --> gán đối tượng này cho data
        tableObject =
            TableEntity.toTableEntity(requireArguments().getString("tableObject").toString())
        if (tableObject == null) {
            findNavController().popBackStack()
        }


        /** Tạo Đối Tượng ViewModel */
        // ViewModelProvider: Lấy&quản lý ViewModels trong 1 LifecycleOwner như 1 Activity or 1 Fragment.
        viewModelCategory = ViewModelProvider(this).get(CategoryViewModel::class.java)
        viewModelCart = ViewModelProvider(this).get(CartViewModel::class.java)
        viewModelTable = ViewModelProvider(this).get(TableViewModel::class.java)
        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)

        /** ----------------------------------------------------------------------------*/
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy được customer khi thêm order mới thì
        viewModelCustomer.getIDWhenInsertOrderSuccess.observe(viewLifecycleOwner){ customerId ->
            idCustomer = customerId
        }

        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        /** Code for Back */
        binding.igmBackOfOrder.setOnClickListener {
            // Nếu là bàn Order thêm (status = 2) thì không làm gì
            if (tableObject!!.table_status_id == 1) {
                tableObject!!.table_status_id = 0
            }
            viewModelTable.addTable(requireContext(), tableObject!!)
            findNavController().popBackStack()
        }

        /** Code for Search */
        //Lọc listOrderItem theo Text và update lại listData cho adapter!
        binding.edtSearchOrderItem.doOnTextChanged { text, start, before, count ->
            if (text.toString().length > 1) {
                searchItem(text.toString())
            } else {
                adapterOrderItem.setListData(listItemOfCategory)
            }
        }
        /** Code for Clear Button */
        binding.txtClear.setOnClickListener {
            // Không dùng listCartItem.clear(). Tránh Crash
            listCartItem = ArrayList()
            adapterCartItem.setListData(listCartItem)
        }
        /** ----------------------------------------------------------------------------*/
        /** Handle data Object: tableEntity above*/
        tableObject?.let { table ->
            // Code cho tên Table
            binding.txtTableNameInOrderList.text = table.table_name
            // Khi có Data thì mới thực hiện getListCategory. Lấy listData từ DB đổ lên View MenuOrder
            getListCategory(chooseCategory)

            // Khi có Table (Đã click chọn Table) mới bắt đầu tạo Order
            // Tạo trước 1 order default
            orderObject = OrderEntity(
                DateFormatUtil.getTimeForOrderCreateTime(),
                idCustomer.toInt(),
                table.table_id,
                SharedPreferencesUtils.getAccountId(),
                DateFormatUtil.getTimeForOrderCreateTime(),
                "",
                0f,
                0
            )

            // Vừa vào NewOrder là đã phải thay đổi trạng thái của bàn rồi. Tránh việc 2 bàn cùng order 1 lúc
            table.table_status_id = 1
            // Thêm bàn vào mà làm gì?
            viewModelTable.addTable(requireContext(), table)

        }

        /** ----------------------------------------------------------------------------*/

        /** Code for Order Button */
        binding.txtOrder.setOnClickListener {
            // Add Order (Bill) vào OrderEntity, thay đôi status sang 1 (Đã được click order)
            orderObject?.let { order ->
                order.order_status_id = 1
                order.customer_id = idCustomer.toInt()
                viewModelCart.addOrder(order)
            }
            // Add list Item_in_Cart into CartItemEntity. Lúc này mới viết vào Database!
            viewModelCart.addListCartItem(listCartItem)
            // Cập nhập trạng thái cho Table
            // Chú ý: 2 thằng không thể order cùng lúc cùng 1 cái bàn được
            /** ----------------------------------------------------*/
            tableObject?.table_status_id = 2
            viewModelTable.addTable(requireContext(), tableObject!!)

            findNavController().popBackStack()
        }
        /** Code for Customer TextView */
        binding.txtCustomerInOrder.setOnClickListener {
            showDialogCustomer()
        }


        /** -------------------------------ADAPTER-------------------------*/
        /** Adapter 1: CATEGORY in BOTTOM:  Xử lý adapter, inflate for View*/
        // 1. Tạo 1 adapter
        adapterCategoryInBottomOfOrderFragment = CategoryInBottomOfOrderFragmentAdapter(
            requireContext(),
            ArrayList(),
            chooseCategory,
            viewLifecycleOwner,
            /** Phần này chưa rõ!*/
            object : CategoryInBottomOfOrderFragmentAdapter.EventClickCategoryInOrderListener {
                override fun clickCategoryInOrder(chooseCategory: Int) {
                    NewOrderFragment().chooseCategory = chooseCategory
                    adapterCategoryInBottomOfOrderFragment.setChooseCategory(chooseCategory)

                    // Get listItem của thằng được chọn --> Thực hiện các thao tác ở dưới hàm.
                    getItemOfCategory(chooseCategory)
                }
            }
        )
        // 2. Dùng adapter vừa tạo cho View cần dùng
        binding.rycCategoryInOrder.adapter = adapterCategoryInBottomOfOrderFragment

        /** Adapter 2:ORDER ITEM:  Xử lý adapter, inflate for View*/
        // 1. Tạo 1 adapter
        adapterOrderItem = ItemOfCategoryInBottomOfOrderFragmentAdapter(
            requireContext(),
            ArrayList(),
            object : ItemOfCategoryInBottomOfOrderFragmentAdapter.EventClickOrderItemListener {
                override fun clickAddOrderItem(itemInCategory: ItemEntity) {
                    // Xử lý Nhấp Add tiếp thì sẽ tăng Order_Quantity ở Cart
                    for (i in 0 until listCartItem.size) {
                        if (listCartItem[i].item_id == itemInCategory.item_id) {
                            listCartItem[i].order_quantity++
                            adapterCartItem.setListData(listCartItem)
                            return
                        }
                    }

                    // Đưa OrderItem vừa Add vào --> Cart
                    listCartItem.add(
                        CartItemEntity(
                            0,
                            itemInCategory.item_id,
                            orderObject!!.order_id,
                            1,
                            // Note thì xíu mới thêm
                            "",
                            DateFormatUtil.getTimeForKitchen(),
                            // 0: Waiting
                            0
                        )
                    )

                    // Set Data (listCartItem) cho adapter --> Đổ lên View CartOrder
                    adapterCartItem.setListData(listCartItem)
                }

            }
        )
        // 2. Dùng adapter vừa tạo cho View cần dùng
        binding.rycOrderItem.adapter = adapterOrderItem

        /** Adapter 3 :CART ITEM:  Xử lý adapter, inflate for View*/
        // 1. Tạo 1 adapter
        adapterCartItem = CartItemAdapter(
            requireParentFragment(),
            ArrayList(),
            viewLifecycleOwner,
            object : CartItemAdapter.EventClickCartItemListener {
                override fun clickMinus(orderedItem: CartItemEntity, pos: Int) {
                    if (listCartItem[pos].order_quantity == 1) {
                        listCartItem.remove(orderedItem)
                    } else {
                        listCartItem[pos].order_quantity--
                    }
                    adapterCartItem.setListData(listCartItem)
                }

                override fun clickPlus(orderedItem: CartItemEntity, pos: Int) {
                    listCartItem[pos].order_quantity++
                    adapterCartItem.setListData(listCartItem)
                }

                override fun clickNote(orderedItem: CartItemEntity, pos: Int) {
                    showDialogNote(orderedItem, pos)
                    adapterCartItem.setListData(listCartItem)
                }

                override fun clickDelete(orderedItem: CartItemEntity, pos: Int) {
                    listCartItem.remove(orderedItem)
                    adapterCartItem.setListData(listCartItem)
                }
            }
        )
        // 2. Dùng adapter vừa tạo cho View cần dùng
        binding.rycCartItemList.adapter = adapterCartItem
    }
    /** ----------------------------------------------------------*/
    /** Note Dialog */
    private fun showDialogNote(orderedItem: CartItemEntity, pos: Int) {
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_add_note, null)
        build.setView(view)
        // ------------------------------------------------------------------------
        val edtNote = view.findViewById<EditText>(R.id.edtNote)

        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        // ------------------------------------------------------------------------
        // 4.  Add Button: Thêm note và hiện note lên cartItem đã note
        btnAdd.setOnClickListener {
            listCartItem[pos].note = edtNote.text.toString()
            adapterCartItem.setListData(listCartItem)
            dialog.dismiss()
        }


        // Other:  Dau X  &   Cancel Button
        imgClose.setOnClickListener { dialog.dismiss() }
        btnCancel.setOnClickListener { dialog.dismiss() }

        // End: Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()

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
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_add_customer, null)
        build.setView(view)
        // 4.  Get Component of Dialog
        val edtPhoneNumber = view.findViewById<EditText>(R.id.edtPhoneNumber)
        val rcyCustomerInPhone = view.findViewById<RecyclerView>(R.id.rcyCustomerInPhone)
        val edtCustomerName = view.findViewById<EditText>(R.id.edtCustomerName)
        val txtCustomerBirthday = view.findViewById<TextView>(R.id.txtCustomerBirthday)
        val btnAddCustomer = view.findViewById<Button>(R.id.btnAddCustomer)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val imgDate = view.findViewById<ImageView>(R.id.imgDate)
        val imgCloseDialogCustomer = view.findViewById<ImageView>(R.id.imgCloseDialogCustomer)
        // -----------------Code for Component----------------------------------------//
        // 1.  Handle Adapter CustomerPhone + Code of clickCustomerInner (Get CustomerInfo and set to View in Order)
        adapterCustomerInner = CustomerInnerAdapter(requireParentFragment(), ArrayList(), object :
            CustomerInnerAdapter.EventClickItemCustomerInnerListener {
            override fun clickCustomerInner(itemCustomer: CustomerEntity) {
                // Có sẵn thì pick-up ra thôi
                customerObject = itemCustomer
                // Lưu thằng customer_id lại để set cho Order
                idCustomer = itemCustomer.customer_id.toLong()


//                orderObject?.customer_id = itemCustomer.customer_id
                // Tìm cách đưa Customer's Name lên NewOrderFragment
                binding.txtCustomerInOrder.text = itemCustomer.customer_name
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
                        binding.txtCustomerInOrder.text = listCustomer[0].customer_name
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

    /** ----------------------------------------------------------*/
    // Set listData get được từ DB cho listData mà Adapter sử dụng, để đổ ra View.
    // Adapter:  CategoryInBottomOfOrderFragmentAdapter
    private fun getListCategory(chooseCategory: Int) {
        viewModelCategory.getAllCategory().observe(viewLifecycleOwner) {
            adapterCategoryInBottomOfOrderFragment.setListData(it)
            getItemOfCategory(chooseCategory)
            // Không có cái này thì sẽ không hiện gì ở MenuOrder!

        }
    }

    // Set listData get được từ DB cho listData mà Adapter sử dụng, để đổ ra View.
    // Adapter:  ItemOfCategoryInBottomOfOrderFragmentAdapter
    private fun getItemOfCategory(category_id: Int) {
        viewModelCategory.getListCategoryComponentItem(category_id)
            .observe(viewLifecycleOwner) { listItem ->
                adapterOrderItem.setListData(listItem)
                // Khi get được ra thì đổ vô listItemOfCategory --> Xử lý Filter!
                listItemOfCategory.clear()
                listItemOfCategory.addAll(listItem)
            }
    }


    private fun searchItem(searchString: String) {
        val filterList = ArrayList<ItemEntity>()
        listItemOfCategory.forEach { item ->
            if (item.item_name.contains(searchString)) {
                filterList.add(item)
            }
        }
        // Adapter: Bây giờ source of Data là filterList
        adapterOrderItem.setListData(filterList)
    }

}
/** Quy trình sử dụng adapter (Ôn tập) */
/*
1. Khai báo adapter kiểu Adapter vừa tạo
2. Gán giá trị cho Adapter: Truyền vào những tham số cần thiết để tạo ra 1 Adapter Object
   Đoạn này cũng ảo diệu lắm nha!
2+. (Có thể nằm trong bước 2 hoặc cho Fragment kế thừa rồi tách hàm riêng ra)
   Thực hiện phương thức đã cài vào trong Interface

3. Thực hiện chuyển đổi:
   binding.rcy.adater = adapterInStep2
4. Sử dụng ViewModel để đưa listData (Category) get được, lên View
 */

/** Quy trình sử dụng ViewModel (Ôn tập) */
/*
1. Khai báo biến viewModel kiểu ViewModel vừa tạo/đã có
2. Tạo 1 đối tượng ViewModel, để tương tác với Fragment này.
3. Thực hiện các event (Các Event này xem ở Item_Category_In_Order
   - Category Được chọn thì: Đổi màu cho CategoryName, getListItem của Category đó ra.
4. Sử dụng ViewModel để đưa listData (Category) get được, lên View
 */

/** Xử lý Order */
/*
1. Tạo Order sau khi đã có Table, gắn vào biến orderEntity
2. Tạo listCartItem = ArrayList<CartItemEntity>() --> Add CartItem --> Adapter đổ lên cho View CartItem
3. Tạo CartViewModel để xử lý Cart and Order
4.
 */
/** Xử lý Order */
/*
1. Những thứ không liên quan đến Adapter thì quăn lên trên xử lý cho dễ nhìn

 */
