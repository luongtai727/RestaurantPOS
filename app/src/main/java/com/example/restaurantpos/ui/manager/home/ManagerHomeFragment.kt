package com.example.restaurantpos.ui.manager.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentManagerHomeBinding
import com.example.restaurantpos.network.WeatherResponse
import com.example.restaurantpos.network.WeatherRetrofitClient
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.showToast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class ManagerHomeFragment : Fragment() {

    lateinit var binding: FragmentManagerHomeBinding
    lateinit var viewModelHome: HomeViewModel


    private val calendar = Calendar.getInstance()
    private val nowYear = calendar.get(Calendar.YEAR)
    private val nowMonth = calendar.get(Calendar.MONTH) + 1
    private val nowDay = calendar.get(Calendar.DAY_OF_MONTH)


    var revenue = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagerHomeBinding.inflate(inflater, container, false)
        viewModelHome = ViewModelProvider(this).get(HomeViewModel::class.java)
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


        /** Call API */

        // Thực tế thì cần get vị trí hiện tại ==> Dùng Trình đọc địa chỉ của Gooogle ==> Liên quan đến Firebase
        val call = WeatherRetrofitClient.weatherAPIInterface.getWeather(
            35.6916,
            139.768,
            "381f382c474ccaadce8927aea11ac978"
        )

        // enqueue: Thêm một công việc mới vào cuối hàng đợi
        // Mình đang dùng thằng callback thuần túy nhất\
        // Thực chất chính là thằng này ở interface: Call<WeatherResponse>
        // Chú ý import Callback của đúng thằng Retrofit2 nha.
        call.enqueue(object : Callback<WeatherResponse> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                val weatherData = response.body()?.main

                if (weatherData != null) {
                    val tempMax = weatherData.temp_max
                    val tempCurrent = weatherData.temp
                    val tempMin = weatherData.temp_min

                    String.format("%.2f °C", tempMin - 273.15)

                    binding.txtMinTemp.text = String.format("%.1f °C", tempMin - 273.15)
                    binding.txtNowTemp.text = String.format("%.1f °C", tempCurrent - 273.15)
                    binding.txtMaxTemp.text = String.format("%.1f °C", tempMax - 273.15)

                    val weatherDescription = response.body()?.weather?.getOrNull(0)?.description
                    val weatherMainDescription = response.body()?.weather?.getOrNull(0)?.main

                    binding.txtWeatherDescription.text =
                        "$weatherMainDescription: $weatherDescription"

                    val weatherIcon = response.body()?.weather?.getOrNull(0)?.icon
                    val weatherIconUrl = "http://openweathermap.org/img/w/$weatherIcon.png"
                    Picasso.get().load(weatherIconUrl).into(binding.imgWeather)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                context?.showToast("Failed to call the API: ${t.message}")
                Log.d("Quanglt", "${t.message}")
            }
        })


        /**=================================================================*/
        /** Shift */
        binding.btnShift.setOnClickListener {
            findNavController().navigate(R.id.action_mainManagerFragment_to_shiftFragment)
        }

        /** Statistic */
        binding.txtStatistic.setOnClickListener {
            findNavController().navigate(R.id.action_mainManagerFragment_to_statisticFragment)
        }

        /** Statistic In Home */
        // Đây là code cơ bản để hiểu vấn đề
        /*        val barEntry = ArrayList<BarEntry>()
        barEntry.add(BarEntry(1f, 2f))
        barEntry.add(BarEntry(2f, 1f))
        barEntry.add(BarEntry(3f, 3f))
        barEntry.add(BarEntry(4f, 2f))
        barEntry.add(BarEntry(5f, 6f))
        barEntry.add(BarEntry(6f, 5f))
        barEntry.add(BarEntry(7f, 1f))

        val barDataSet = BarDataSet(barEntry, "Test 01")
        val barData = BarData(barDataSet)
        binding.chart.data = barData

        binding.chart.setVisibleXRangeMaximum(5f)
        binding.chart.moveViewToX(0.5f)*/


        /*        viewModelHome.getRevenueOfDay("2023/07")
                viewModelHome.getRevenueOfDayOfItem(1, "2023/07")*/


        // Title
        val graph_label = ArrayList<String>()
        for (i in 1..DataUtil.getNumberOfDayInMonth(nowYear, nowMonth)) {
            graph_label.add("Day $i")
        }

        // Data đi cùng
        val listRevenue = ArrayList<Float>()
        viewModelHome.getRevenueOfDay(nowYear, nowMonth)
        // Add vào và vẽ liên tục
        // Đến gì = -1. Tức là add xong hết rồi
        viewModelHome.isDuplicate.observe(viewLifecycleOwner) {
            if (it == -1f)
                create_graph(binding.chart, graph_label, listRevenue)
            else {
                listRevenue.add(it)
            }
        }




//        for (i in 1..DataUtil.getNumberOfDayInMonth(nowYear, nowMonth)) {
//            CoroutineScope(Dispatchers.IO).launch {
//                revenue = DatabaseUtil.getRevenueOfDay("$nowYear/$nowMonth/$i")
//                listRevenue.add(revenue)
//            }
//        }


        /*                listRevenue.add(5f)
                        listRevenue.add(8f)
                        listRevenue.add(10f)
                        listRevenue.add(51f)
                        listRevenue.add(5f)
                        listRevenue.add(53f)
                        listRevenue.add(5f)
                        listRevenue.add(56f)
                        listRevenue.add(35f)
                        listRevenue.add(45f)
                        listRevenue.add(5f)
                        listRevenue.add(15f)
                        listRevenue.add(15f)
                        listRevenue.add(15f)
                        listRevenue.add(25f)
                        listRevenue.add(25f)
                        listRevenue.add(21f)
                        listRevenue.add(21f)
                        listRevenue.add(21f)
                        listRevenue.add(15f)
                        listRevenue.add(15f)
                        listRevenue.add(15f)
                        listRevenue.add(15f)
                        listRevenue.add(11f)
                        listRevenue.add(11f)
                        listRevenue.add(11f)
                        listRevenue.add(11f)
                        listRevenue.add(21f)
                        listRevenue.add(21f)
                        listRevenue.add(21f)
                        listRevenue.add(21f)*/

//        create_graph(binding.chart, graph_label, listRevenue)

        /**=================================================================*/
    }

    // Item của adapter là biểu đồ luôn cũng okay
    // Layout adapter chính là cái char
    // Bên trong adapter thì lọc cái graph_lable vaf listDoanhThu ra.
    private fun create_graph(chart: BarChart, graph_label: List<String?>?, userScore: List<Float>) {
        try {
            chart.setDrawBarShadow(false)
            chart.setDrawValueAboveBar(true)
            chart.description.isEnabled = false
            chart.setPinchZoom(false)
            chart.setDrawGridBackground(false)
            val yAxis: YAxis = chart.axisLeft
            yAxis.valueFormatter =
                IAxisValueFormatter { value, axis -> value.toInt().toString() }
            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            yAxis.granularity = 1f
            yAxis.isGranularityEnabled = true
            chart.axisRight.isEnabled = false
            val xAxis: XAxis = chart.xAxis
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(graph_label)
            val yVals1: MutableList<BarEntry> = ArrayList()
            for (i in userScore.indices) {
                yVals1.add(BarEntry(i * 1.0f + 0.5f, userScore[i]))
            }
            val set1: BarDataSet
            if (chart.data != null && chart.data.dataSetCount > 0) {
                set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                set1.values = yVals1
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
            } else {
                set1 = BarDataSet(yVals1, "Revenue Of 2023/07")
                set1.color = Color.rgb(242, 15, 15)
                val dataSets = ArrayList<IBarDataSet>()
                dataSets.add(set1)
                val data = BarData(dataSets)
                chart.data = data
            }
            chart.setFitBars(true)
            val l: Legend = chart.legend
            l.formSize = 12f
            l.form = Legend.LegendForm.SQUARE
            l.position = Legend.LegendPosition.RIGHT_OF_CHART_INSIDE
            l.textSize = 10f
            l.textColor = Color.BLACK
            l.xEntrySpace = 5f
            l.yEntrySpace = 5f
            chart.invalidate()
            chart.animateY(2000)
            binding.chart.setVisibleXRangeMaximum(5f)
            binding.chart.moveViewToX(0.5f)

        } catch (ignored: Exception) {

        }
    }


}