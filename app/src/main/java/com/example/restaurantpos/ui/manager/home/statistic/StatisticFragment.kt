package com.example.restaurantpos.ui.manager.home.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.databinding.FragmentStatisticBinding

/*
Ở màn home chỉ hiển thị đơn thuần doanh thu của ngày hôm nay thôi
Title:
 - Ở Home là ngày. Bỏ qua ngày ở Fragment
 - Ở Fragment --> Truyền vào tuần/tháng/năm sẽ có tuần/tháng/năm
*/
class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding
    lateinit var adapterStatistic: StatisticAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** imgBack */
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        /** Adapter */
        val listData = ArrayList<ModelStatistic>()
        listData.add(ModelStatistic("January", 1.0f))
        listData.add(ModelStatistic("February", 2.0f))
        listData.add(ModelStatistic("March", 3.0f))
        listData.add(ModelStatistic("April", 4.0f))
        listData.add(ModelStatistic("May", 5.0f))
        listData.add(ModelStatistic("June", 6.0f))
        listData.add(ModelStatistic("July", 7.0f))
        listData.add(ModelStatistic("August", 8.0f))
        listData.add(ModelStatistic("September", 9.0f))
        listData.add(ModelStatistic("October", 10.0f))
        listData.add(ModelStatistic("November", 11.0f))
        listData.add(ModelStatistic("December", 12.0f))

        adapterStatistic = StatisticAdapter(requireContext(), ArrayList<ModelStatistic>(), 15f)
        binding.rcyStatistic.adapter = adapterStatistic
        adapterStatistic.setListData(listData)

    }
}