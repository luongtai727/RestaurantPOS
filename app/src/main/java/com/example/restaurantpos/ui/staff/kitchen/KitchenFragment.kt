package com.example.restaurantpos.ui.staff.kitchen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentKitchenBinding
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.ui.login.LoginActivity
import com.example.restaurantpos.ui.staff.receptionist.order.CartViewModel
import com.example.restaurantpos.util.DatabaseUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.openActivity
import com.example.restaurantpos.util.showToast

// Nhận định: Thằng này sử dụng lại ViewModel của CartViewModel
class KitchenFragment : Fragment() {

    lateinit var binding: FragmentKitchenBinding

    lateinit var viewModelCart: CartViewModel

    lateinit var adapterCartItemInKitchen: CartItemInKitchenAdapter

    lateinit var dialog: AlertDialog

    /*
    Sort theo Order_id (Order_create_id)
    sortByTimeOfOrder = 0 --> Không Sort/Giữ nguyên tăng dần      Ascending
    sortByTimeOfOrder = 1 --> Sort ngược (Giảm dần)               Descending
    sortByTimeOfOrder = 2 --> Bỏ qua

    Chuyển sortByTimeOfOrder sang MutableLiveData --> Lắng nghe
    */
    private var sortByTimeOfOrder = MutableLiveData(0)
    private var sortByTable = MutableLiveData(0)
    private var sortByStatus = MutableLiveData(0)
    private var sortByItemName = MutableLiveData(0)
    private var sortByOrderQuantity = MutableLiveData(0)
    private var sortByNote = MutableLiveData(0)

//    var listCartItem = ArrayList<CartItemEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKitchenBinding.inflate(inflater, container, false)

        /** Tạo Đối Tượng ViewModel */
        // ViewModelProvider: Lấy & quản lý ViewModels trong 1 LifecycleOwner như 1 Activity or 1 Fragment.
        viewModelCart = ViewModelProvider(this).get(CartViewModel::class.java)


        return binding.root
    }


    // Thêm một biến để kiểm soát việc sắp xếp danh sách
    private var isSortingEnabled = true

    fun pauseSorting() {
        isSortingEnabled = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        /** ToolBar */
        // 1. Get Account's Name
        binding.txtLoginAccountName.text = SharedPreferencesUtils.getAccountName()
        // 2. Logout Button
        binding.imgMenuToolBar.setOnClickListener { it ->
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.inflate(R.menu.popup_menu_main_manager)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_logout -> {
                        context?.openActivity(LoginActivity::class.java, true)
                        true
                    }

                    R.id.menu_update_info -> {
                        findNavController().navigate(R.id.action_kitchenFragment_to_updateStaffInfoFragment)
                        true
                    }

                    else -> true
                }
            }

            try {
                val popup = popupMenu::class.java.getDeclaredField("qPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                popupMenu.show()
            }
        }

        /** Kitchen Shift Show */
        binding.txtShift.setOnClickListener {
            findNavController().navigate(
                R.id.action_kitchenFragment_to_shiftOfStaffFragment,
                bundleOf("shiftOfStaff" to 2)
            )
        }
        /** ----------------------------------------------------------------------------------*/

        /** Adapter CartItemInKitchen:  Xử lý adapter, inflate for View*/
        // Luôn nhìn từ setListData ra
        // 1. Tạo 1 adapter
        adapterCartItemInKitchen = CartItemInKitchenAdapter(
            requireContext(),
            viewLifecycleOwner,
            ArrayList(),
            object : CartItemInKitchenAdapter.EventClickCartItemInKitchenListener {
                override fun clickCartItemStatus(cartItemInKitchen: CartItemEntity) {
                    if (cartItemInKitchen.cart_item_status_id == 1) {

                        showConfirmItemStatusDialog(cartItemInKitchen)
                    } else {
                        // Kiểm soát vấn đề cart_item_status_id thay đổi thì Hàm SORT cũng thay đổi theo
                        pauseSorting()
                        cartItemInKitchen.cart_item_status_id++
                        viewModelCart.addCartItem(cartItemInKitchen)

                    }
                }
            })
        // 2. Dùng adapter vừa tạo cho View cần dùng
        binding.rcyCartItemInKitchen.adapter = adapterCartItemInKitchen
        // 3. Set data cho adapter chuyển đổi.
        // Thêm quả Sort!. Sort bao nhiêu thằng thì truyền vào bấy nhiêu thằng
        // sortByTimeOfOrder sang MutableLiveData --> Lắng nghe --> Sort thay đổi thì cập nhật lại listData

        sortByTimeOfOrder.observe(viewLifecycleOwner) {
            viewModelCart.getListCartItemOfKitchenBySortTime(sortByTimeOfOrder.value!!)
                .observe(viewLifecycleOwner) { listCart ->
                    adapterCartItemInKitchen.setListData(listCart as ArrayList<CartItemEntity>)
                }
        }

        /** SORT theo TITlE */
        // Muốn sort cái gì thì cứ cập nhập cái list đó vào

        // 1. Theo TIME
        binding.layoutTitleOfCartItemInKitchen.txtTime.setOnClickListener {
            when (sortByTimeOfOrder.value) {
                1 -> sortByTimeOfOrder.value = 0
                0 -> sortByTimeOfOrder.value = 1
            }
        }

        // 2. Theo TABLE
        binding.layoutTitleOfCartItemInKitchen.txtTableName.setOnClickListener {

            sortByTable.observe(viewLifecycleOwner) {
                viewModelCart.getListCartItemOfKitchenSortByTable(sortByTable.value!!)
                    .observe(viewLifecycleOwner) { listCart ->
                        adapterCartItemInKitchen.setListData(listCart as ArrayList<CartItemEntity>)
                    }
            }

            when (sortByTable.value) {
                1 -> sortByTable.value = 0
                0 -> sortByTable.value = 1
            }

            /*    when (sortByTable.value) {
                    1 -> sortByTable.value = 2
                    2 -> sortByTable.value = 0
                    else -> sortByTable.value = 1
                }*/
        }

        // 3. Theo STATUS
        binding.layoutTitleOfCartItemInKitchen.txtCondition.setOnClickListener {
            isSortingEnabled = true
            if (isSortingEnabled) {
                sortByStatus.observe(viewLifecycleOwner) {
                    viewModelCart.getListCartItemOfKitchenSortByStatus(sortByStatus.value!!)
                        .observe(viewLifecycleOwner) {

                                listCart ->
                            adapterCartItemInKitchen.setListData(listCart as ArrayList<CartItemEntity>)
                        }
                }

                when (sortByStatus.value) {
                    1 -> sortByStatus.value = 0
                    0 -> sortByStatus.value = 1
                }
            }
        }

        // 4. Theo ITEM NAME
        binding.layoutTitleOfCartItemInKitchen.txtItemName.setOnClickListener {

            sortByItemName.observe(viewLifecycleOwner) {
                viewModelCart.getListCartItemOfKitchenSortByItemName(sortByItemName.value!!)
                    .observe(viewLifecycleOwner) { listCart ->
                        adapterCartItemInKitchen.setListData(listCart as ArrayList<CartItemEntity>)
                    }
            }

            when (sortByItemName.value) {
                1 -> sortByItemName.value = 0
                0 -> sortByItemName.value = 1
            }
        }

        // 5. Theo ORDER QUANTITY
        binding.layoutTitleOfCartItemInKitchen.txtOrderQuantity.setOnClickListener {

            sortByOrderQuantity.observe(viewLifecycleOwner) {
                viewModelCart.getListCartItemOfKitchenSortByOrderQuantity(sortByOrderQuantity.value!!)
                    .observe(viewLifecycleOwner) { listCart ->
                        adapterCartItemInKitchen.setListData(listCart as ArrayList<CartItemEntity>)
                    }
            }

            when (sortByOrderQuantity.value) {
                1 -> sortByOrderQuantity.value = 0
                0 -> sortByOrderQuantity.value = 1
            }
        }

        // 6. Theo NOTE
        binding.layoutTitleOfCartItemInKitchen.txtNote.setOnClickListener {

            sortByNote.observe(viewLifecycleOwner) {
                viewModelCart.getListCartItemOfKitchenSortByNote(sortByNote.value!!)
                    .observe(viewLifecycleOwner) { listCart ->
                        adapterCartItemInKitchen.setListData(listCart as ArrayList<CartItemEntity>)
                    }
            }

            when (sortByNote.value) {
                1 -> sortByNote.value = 0
                0 -> sortByNote.value = 1
            }
        }
    }


    private fun showConfirmItemStatusDialog(cartItemInKitchen: CartItemEntity) {
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_confirm_status_in_kitchen, null)
        build.setView(view)


        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnRevert = view.findViewById<Button>(R.id.btnRevert)
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)


        imgClose.setOnClickListener { dialog.dismiss() }


        btnDone.setOnClickListener {
            pauseSorting()
            DatabaseUtil.getItemOfCategory(cartItemInKitchen.item_id)
                .observe(viewLifecycleOwner) {
                    context?.showToast("Done ${it[0].item_name}. Send to Receptionist")
                }
            cartItemInKitchen.cart_item_status_id++
            viewModelCart.addCartItem(cartItemInKitchen)
            dialog.dismiss()
        }

        btnRevert.setOnClickListener {
            pauseSorting()
            cartItemInKitchen.cart_item_status_id--
            viewModelCart.addCartItem(cartItemInKitchen)
            dialog.dismiss()
        }
        // End. Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }
}
