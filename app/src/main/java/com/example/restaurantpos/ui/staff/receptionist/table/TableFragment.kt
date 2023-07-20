package com.example.restaurantpos.ui.staff.receptionist.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentTableBinding
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.ui.login.LoginActivity
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.openActivity

class TableFragment : Fragment(), TableAdapter.EventClickTableListener {

    lateinit var binding: FragmentTableBinding
    lateinit var adapter: TableAdapter
    private lateinit var viewModel: TableViewModel


    lateinit var dialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTableBinding.inflate(inflater, container, false)
        return binding.root
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


        /** ViewModel --> Handle Database */
        viewModel = ViewModelProvider(this).get(TableViewModel::class.java)

        viewModel.getAllTable().observe(viewLifecycleOwner) {
            adapter.setListData(it)
        }


        /** Adapter */
        adapter = TableAdapter(requireContext(), mutableListOf(), this)
        binding.rcyTable.adapter = adapter

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
                        requireContext().openActivity(LoginActivity::class.java, true)
                        true
                    }

                    R.id.menu_update_info -> {
                        findNavController().navigate(R.id.action_tableFragment_to_updateStaffInfoFragment)
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
                R.id.action_tableFragment_to_shiftOfStaffFragment,
                bundleOf("shiftOfStaff" to 1)
            )
        }

    }

    /** clickTable --> Navigate to Order/Ordered Fragment */
    /** bundleOf(26 and 55) */
    override fun clickTable(itemTable: TableEntity, table_status: Int) {
        if (table_status == 0) {
            findNavController().navigate(
                R.id.action_tableFragment_to_orderFragment,
                bundleOf("tableObject" to itemTable.toJson())
            )
        }
        if (table_status == 2) {
            findNavController().navigate(
                R.id.orderedTableFragment,
                bundleOf("tableObject" to itemTable.toJson())
            )
        }
    }

    override fun changeStatus(itemTable: TableEntity, table_status: Int) {
        if (table_status == 0) {
            showDisableTableDialog(itemTable)
        }

        if (table_status == 1) {
            showEnableDialog(itemTable)
        }

    }


    private fun showDisableTableDialog(itemTable: TableEntity) {
        // -----------------Prepare--------------------------------------------------//
        // 1.  Build Dialog
        // 2.  Designed XML --> View
        // 3.  Set VIEW tra ve above --> Dialog
        val build = AlertDialog.Builder(requireContext(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_change_table_status, null)
        build.setView(view)
        // 4.  Get Component of Dialog
        val btnDisable = view.findViewById<Button>(R.id.btnDisable)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val edtChangeReason = view.findViewById<EditText>(R.id.edtChangeReason)
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        // -----------------Code for Component----------------------------------------//
        // 5.  Disable Table

        btnDisable.setOnClickListener {
            if (edtChangeReason.text.isEmpty()) {
                edtChangeReason.hint = "Reason must not be empty!"
            } else {
                itemTable.table_status_id = 1
                itemTable.table_name = edtChangeReason.text.toString()
                viewModel.addTable(requireContext(), itemTable)
                viewModel.getAllTable().observe(viewLifecycleOwner) {
                    adapter.setListData(it)
                }
                dialog.dismiss()
            }
        }

        // Other:  Dau X  &   Cancel Button
        btnCancel.setOnClickListener { dialog.dismiss() }
        imgClose.setOnClickListener { dialog.dismiss() }

        // End: Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }

    private fun showEnableDialog(itemTable: TableEntity) {

        // -----------------Prepare--------------------------------------------------//
        // 1.  Build Dialog
        // 2.  Designed XML --> View
        // 3.  Set VIEW tra ve above --> Dialog
        val build = AlertDialog.Builder(requireContext(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_enable_table, null)
        build.setView(view)
        // 4.  Get Component of Dialog
        val btnEnable = view.findViewById<Button>(R.id.btnEnable)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val edtTableName = view.findViewById<EditText>(R.id.edtTableName)
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        // -----------------Code for Component----------------------------------------//
        // 5.  Disable Table

        btnEnable.setOnClickListener {
            if (edtTableName.text.isEmpty()) {
                edtTableName.hint = "Table's name must not be empty!"
            } else {
                itemTable.table_status_id = 0
                itemTable.table_name = edtTableName.text.toString()
                viewModel.addTable(requireContext(), itemTable)
                viewModel.getAllTable().observe(viewLifecycleOwner) {
                    adapter.setListData(it)
                }
                dialog.dismiss()
            }
        }

        // Other:  Dau X  &   Cancel Button
        btnCancel.setOnClickListener { dialog.dismiss() }
        imgClose.setOnClickListener { dialog.dismiss() }

        // End: Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }


}
