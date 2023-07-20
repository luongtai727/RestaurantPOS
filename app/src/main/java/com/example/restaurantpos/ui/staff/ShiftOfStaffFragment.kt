package com.example.restaurantpos.ui.staff

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
import com.example.restaurantpos.databinding.FragmentShiftOfStaffBinding
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountShiftEntity
import com.example.restaurantpos.ui.manager.home.shift.ShiftAdapter
import com.example.restaurantpos.ui.manager.home.shift.ShiftViewModel
import com.example.restaurantpos.ui.manager.home.shift.StaffSelectionAdapter
import com.example.restaurantpos.ui.manager.user.UserViewModel
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import java.util.Calendar

class ShiftOfStaffFragment : Fragment() {
    lateinit var binding: FragmentShiftOfStaffBinding
    private lateinit var adapterShift: ShiftAdapter
    private lateinit var adapterStaffSelection: StaffSelectionAdapter

    private lateinit var viewModelShift: ShiftViewModel
    private lateinit var viewModelUser: UserViewModel


    private var year = 2023
    private var month = 7
    private var day = 10

    // Add account_shift
    lateinit var dialog: AlertDialog
    val calendar = Calendar.getInstance()

    private var staffObject: AccountEntity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShiftOfStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** ViewModel */
        viewModelShift = ViewModelProvider(this).get(ShiftViewModel::class.java)
        viewModelUser = ViewModelProvider(this).get(UserViewModel::class.java)

        /**---------------------------------------------------------------------------------------*/
        // Vừa vào là đã phải hiển thị NOW TIME (year + month + day) rồi nhé
        binding.txtDateInShift.text = "$year/$month"

        // Trả về hôm nay, và các ngày tiếp theo được xử lý tự động
        day = getFirst()

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
        /** imgMonthBack */
        binding.imgMonthBack.setOnClickListener {
            backWeek()
        }

        /** imgMonthNext */
        binding.imgMonthNext.setOnClickListener {
            nextWeek()
        }

        /**---------------------------------------------------------------------------------------*/
        /** Adapter */
        adapterShift = ShiftAdapter(
            requireContext(),
            viewLifecycleOwner,
            ArrayList(),
            year,
            month,
            day,
            object : ShiftAdapter.EventClickShiftListener {
                override fun clickMorningShift(shift_id: String) {
                    if (SharedPreferencesUtils.getAccountRole() == 0) {
                        viewModelShift.addAccountShift(
                            AccountShiftEntity(0, shift_id, 2)
                        )
//                        showAddAccountShiftDialog(shift_id)
                    }
                }

                override fun clickAfternoonShift(shift_id: String) {
                    if (SharedPreferencesUtils.getAccountRole() == 0) {
                        viewModelShift.addAccountShift(
                            AccountShiftEntity(0, shift_id, 3)
                        )
//                        showAddAccountShiftDialog(shift_id)
                    }
                }

                override fun clickNightShift(shift_id: String) {
                    if (SharedPreferencesUtils.getAccountRole() == 0) {
                        viewModelShift.addAccountShift(
                            AccountShiftEntity(0, shift_id, 2)
                        )
//                        showAddAccountShiftDialog(shift_id)
                    }
                }
            })
        binding.rcyShift.adapter = adapterShift
        /**---------------------------------------------------------------------------------------*/
    }

    private fun backWeek() {
        // Tính lại Năm, Tháng, ngày. Và Set lại data cho adapter
        if (7 >= day) {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                if (month == 1) {
                    month = 12
                    year -= 1
                } else {
                    month -= 1
                }
                day = DataUtil.numberOfDayInAMonthOfLeapYear[month - 1] + (day - 7)
            } else {
                if (month == 1) {
                    month = 12
                    year -= 1
                } else {
                    month -= 1
                }
                day = DataUtil.numberOfDayInAMonthOfNotLeapYear[month - 1] + (day - 7)
            }
        } else {
            day -= 7
        }
        binding.txtDateInShift.text = "$year/$month"
        adapterShift.setDate(year, month, day)
    }

    private fun nextWeek() {
        val now = day + 7

        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
            if (now > DataUtil.numberOfDayInAMonthOfLeapYear[month]) {
                day = now - DataUtil.numberOfDayInAMonthOfLeapYear[month]
                if (month == 12) {
                    month = 1
                    year += 1
                } else {
                    month += 1
                }
            } else {
                day = now
            }
        } else {
            if (now > DataUtil.numberOfDayInAMonthOfNotLeapYear[month]) {
                day = now - DataUtil.numberOfDayInAMonthOfNotLeapYear[month]
                if (month == 12) {
                    month = 1
                    year += 1
                } else {
                    month += 1
                }
            } else {
                day = now
            }
        }
        binding.txtDateInShift.text = "$year/$month"
        adapterShift.setDate(year, month, day)
    }

    /** getFirst --> Monday is ? */
    private fun getFirst(): Int {
        val calendar = Calendar.getInstance()
        val nowDay = calendar.get(Calendar.DATE)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        // 1 là Chủ Nhật và 2 đến 7 là tương ứng
        // Xử lý để thứ 2 là ngày đầu tuần

        when (dayOfWeek) {
            1 -> {
                return nowDay - 6
            }

            2 -> {
                return nowDay
            }

            3 -> {
                return nowDay - 1
            }

            4 -> {
                return nowDay - 2
            }

            5 -> {
                return nowDay - 3
            }

            6 -> {
                return nowDay - 4
            }

            7 -> {
                return nowDay - 5
            }

        }
        return 1
    }
}