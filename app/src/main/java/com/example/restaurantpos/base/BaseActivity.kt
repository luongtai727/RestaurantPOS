package com.example.restaurantpos.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    //    private var _binding: VB? = null
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        _binding = inflaterViewBinding(layoutInflater)
        binding = getInflaterViewBinding(layoutInflater)
        setContentView(binding.root)
        initOnCreate()
//        initView()
//        initData()
//        initListener()

    }

    /**1. Create View
     * 2. Get Data
     * 3. Event Handle
     */
//    abstract fun initView()
//    abstract fun initData()
//    abstract fun initListener()

    abstract fun initOnCreate()

    /*    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Service cũng thế, Destroy thì hủy hết ==> Cần đọc thêm về Service
    }*/

    abstract fun getInflaterViewBinding(layoutInflater: LayoutInflater): VB

    fun closeKeyboard() {

    }
}
