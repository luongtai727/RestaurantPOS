package com.example.restaurantpos.ui.manager.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.R
import com.example.restaurantpos.base.BaseFragment
import com.example.restaurantpos.databinding.FragmentManagerUserBinding
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.show
import com.example.restaurantpos.util.showToast

class ManagerUserFragment : BaseFragment<FragmentManagerUserBinding>() {

    private lateinit var viewModel: UserViewModel
    lateinit var adapter: ManagerUserAdapter
    lateinit var dialog: AlertDialog

    /**
     * Show Account List onto Screen
     */
    override fun initCreate() {
        adapter = ManagerUserAdapter(
            requireActivity(),
            ArrayList(),
            object : ManagerUserAdapter.EventClickItemUserListener {
                override fun clickEditUser(itemUser: AccountEntity) {
                    showEditDialog(itemUser)
                }
            })
        binding.rcyUserManagement.adapter = adapter

        /**  Set data getAllUser() for adapter  */
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.getAllUser().observe(viewLifecycleOwner) {
            adapter.setListData(it)
        }
        //it là một biến ngầm định trong lambda expression, đại diện cho dữ liệu mới nhận được từ LiveData
        // ADD --> Hiện ADD Fragment ra
        binding.imgAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_mainManagerFragment_to_addUserFragment)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun showEditDialog(itemUser: AccountEntity) {
        // 1.  Build Dialog
        // 2.  Designed XML --> View
        // 3.  Set VIEW tra ve above --> Dialog
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_edit_user, null)
        build.setView(view)
        // 4.  Get Component of Dialog
        val txtAccountName = view.findViewById<TextView>(R.id.txtAccountName)
        val txtUserName = view.findViewById<TextView>(R.id.txtUserName)
        val txtBirthday = view.findViewById<TextView>(R.id.txtBirthday)
        val txtPhone = view.findViewById<TextView>(R.id.txtPhone)
        val txtResetPassword = view.findViewById<TextView>(R.id.txtResetPassword)
        val txtLock = view.findViewById<TextView>(R.id.txtLock)
        val txtUnlock = view.findViewById<TextView>(R.id.txtUnlock)
        val txtInform = view.findViewById<TextView>(R.id.txtInform)

        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        //------------------------------------------------------------------------------//
        // 5. Show Info
        txtAccountName.text = itemUser.account_name
        txtBirthday.text = itemUser.account_birthday
        txtPhone.text = itemUser.account_phone
        txtUserName.text = itemUser.user_name

        // 5.  Handle Lock
        txtResetPassword?.setOnClickListener {
            itemUser.password = DataUtil.convertToMD5("123")
            requireContext().showToast("Password was changed  into 123")
            viewModel.addUser(requireContext(), itemUser)
            dialog.dismiss()
        }

        // 6.  Handle Lock
        txtLock?.setOnClickListener {

            if (itemUser.account_status_id) {
                itemUser.account_status_id = false
                requireContext().showToast("${itemUser.account_name} was locked!")
//                itemUser.account_name = "(Locked) " + itemUser.account_name
                viewModel.addUser(requireContext(), itemUser)
                viewModel.getAllUser().observe(viewLifecycleOwner) {
                    adapter.setListData(it)
                }

                dialog.dismiss()
            }else{
                txtInform.setText(R.string.account_disabled_message)
                txtInform.show()
            }
        }

        // 7.  Handle UnLock
        txtUnlock?.setOnClickListener {
            if (!itemUser.account_status_id) {
                itemUser.account_status_id = true
//                itemUser.account_name = itemUser.account_name.substring(8)
                viewModel.addUser(requireContext(), itemUser)

                requireContext().showToast("${itemUser.account_name} was unlocked!")
                viewModel.getAllUser().observe(viewLifecycleOwner) {
                    adapter.setListData(it)
                }

                dialog.dismiss()
            }else{
                txtInform.setText(R.string.account_active_message)
                txtInform.show()
            }
        }

        // Other: Close or Cancel
        imgClose.setOnClickListener { dialog.dismiss() }

        // End. Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }

    override fun getInflateViewBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagerUserBinding {
        return FragmentManagerUserBinding.inflate(layoutInflater, container, false)
    }


}