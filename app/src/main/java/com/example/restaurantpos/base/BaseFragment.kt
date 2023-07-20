package com.example.restaurantpos.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    //    private var _binding: VB? = null
    protected lateinit var binding: VB

    // 1. Phương thức khởi tạo View. View mà mình hay truyền vào Context
    // ==> View không có thì context cũng không có đâu.
    // ==> Nên mọi thứ nên đặt trong onViewCreated() chứ đừng trong onCreateView()
    // Trường hợp đặt trong onCreateView(), và rồi nếu Context Null thì những thằng
    // request về Activity chứa thằng Fragment sẽ Null hết. Null là Crash App luôn
    // Chốt xử lý trong 2!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getInflateViewBinding(inflater, container)
        return requireNotNull(binding).root
    }

    // 2. Thực hiện các tác vụ cần thiết sau khi fragment đã được tạo ở 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCreate()
    }

    abstract fun initCreate()


    abstract fun getInflateViewBinding(layoutInflater: LayoutInflater, container: ViewGroup?): VB


}