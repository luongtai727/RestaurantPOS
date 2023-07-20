package com.example.restaurantpos.ui.manager.home.statistic

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R

/*
Về cái Max sẽ làm 1 cái Model riêng vì
Mình thống kê ra có thể có rất nhiều loại định dạng,
nhưng cũng chỉ đơn giản là mình truyền vào 1 list
với title thì nếu là năm thì truyền vào text sẽ là năm, tuần thì truyền tuần,...
nếu là tháng thì truyền vào là ngày thôi

Sau khi get xong dữ liệu thì mình sẽ lọc nó ra và phân tích nó thành model đấy, mình đổ chung vào
Mình sẽ tái sủ dụng được thằng... ấy
*/

class StatisticAdapter(
    var context: Context,
//    private val lifecycleOwner: LifecycleOwner,
    private var data: ArrayList<ModelStatistic>,
    private var maxRevenue: Float,
) : RecyclerView.Adapter<StatisticAdapter.ViewHolder>() {

    // class ViewHolder --> đại diện cho mỗi item view trong RecyclerView.
    // Thường chứa các thành phần của View --> Để hiển thị cho mỗi item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtTitleByTime = itemView.findViewById<TextView>(R.id.txtTitleByTime)
        var txtRevenue = itemView.findViewById<TextView>(R.id.txtRevenue)
        var viewRevenue = itemView.findViewById<View>(R.id.viewRevenue)
        var viewRootStatisticColumn =
            itemView.findViewById<LinearLayout>(R.id.viewRootStatisticColumn)
    }

//    override fun getItemCount(): Int = 7

    override fun getItemCount() = data.size

    //Method 1: Main in Adapter: XML Layout ==> View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_statistic_collumn, parent, false)
        return ViewHolder(convertedView)
    }

    // Method 2: Bind Each Element in List RESOURCE DATA (OutData Format) ==> Element in designed Layout ==> Display in Screen
    // Position ở đây sẽ ứng dụng position của List trong Database
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemStatisticModel = data[position]
        /** Đổ data lên View */
        holder.txtTitleByTime.text = itemStatisticModel.title
        holder.txtRevenue.text = itemStatisticModel.revenue.toString()

        // Tách ra cũng được --> Tránh trường hợp nó load lâu quá. Thằng này đè view thằng kia.
        showInfo(
            holder.viewRevenue,
            holder.viewRootStatisticColumn,
            itemStatisticModel.revenue
        )
    }

    /**-----------------------------------------------------------------------------------------*/

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data: ArrayList<ModelStatistic>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    private fun showInfo(
        viewRevenue: View,
        viewRootStatisticColumn: LinearLayout,
        revenue: Float
    ) {
        // Max = 100% --> Bọn còn lại chiếu theo thằng Max
        val percent = revenue / maxRevenue
        val height = viewRootStatisticColumn.height * percent
        val layoutParams = viewRevenue.layoutParams
        viewRootStatisticColumn.measure(0,0)
        layoutParams.height = height.toInt()
        viewRevenue.layoutParams = layoutParams
//        viewRevenuePercent.setLayoutParams(ViewGroup.LayoutParams(0, height))


        //        height chỉ có getHeight thôi. Không có set
        //        Nhưng mình vẫn set được. Vì bản chất của thằng Java này, thằng setHeight dang bị private
        //        Vì lúc mà mình set được trong XML, đồng nghĩa với việc để có được file XML, mình phải có file Code
        //        File code này chắc chắn có những dòng quy định việc có cho mình set trong/ngoài XML không (Back end)
        // Cần thì tra mạng thôi. Vì bản chất, set dược trong XML là sẽ làm được trên code dù cho nó có private
        // set height of View Android Studio
        // Ưu tiên thằng có tích V trong StackOverFlow nhé.
        // setLayoutParams --> Thực chất là truy xuất vào code của nó đấy
    }

}


