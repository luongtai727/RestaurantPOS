package com.example.restaurantpos.ui.manager.coupon

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.ActivityCouponBinding
import com.example.restaurantpos.db.entity.CouponEntity
import com.example.restaurantpos.ui.main.MainManagerActivity
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.DatabaseUtil
import com.example.restaurantpos.util.DateFormatUtil
import com.example.restaurantpos.util.hide
import com.example.restaurantpos.util.openActivity
import com.example.restaurantpos.util.show
import com.example.restaurantpos.util.showToast

class CouponActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCouponBinding

    private lateinit var adapterCoupon: CouponAdapter

    private lateinit var viewModelCoupon: CouponViewModel

    private var listCoupon: MutableList<CouponEntity>? = null
    lateinit var dialog: AlertDialog


    private var isSortingEnabled = true

    fun pauseSorting() {
        isSortingEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Thêm một biến để kiểm soát việc sắp xếp danh sách


        /** ViewModel */
        viewModelCoupon = ViewModelProvider(this).get(CouponViewModel::class.java)


        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backToManager()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)

        /** Cancel/Back */
        binding.imgBack.setOnClickListener {
            backToManager()
        }

        /** Handle Adapter */
        adapterCoupon = CouponAdapter(
            this@CouponActivity,
            ArrayList(),
            object : CouponAdapter.EventClickCouponListener {
                override fun clickCoupon(coupon: CouponEntity) {
                    // LongClick: Edit (Date, Code, Discount)
                    showHideDialog(coupon)
                }

                override fun clickCouponStatus(coupon: CouponEntity) {

                    if (coupon.coupon_status == 0) {
                        showConfirmDialog(coupon)
                    } else {
                        // Kiểm soát vấn đề cart_item_status_id thay đổi thì Hàm SORT cũng thay đổi theo
                        coupon.coupon_status = 0
                        viewModelCoupon.addCouponUpdate(coupon)
                    }

                }
            })

        binding.rcyCoupon.adapter = adapterCoupon

        viewModelCoupon.getAllCoupon().observe(this) { allCoupon ->
            adapterCoupon.setListData(allCoupon)
        }
        /**-----------------------------------------------------------------------*/
        DataUtil.setEditTextWithoutSpecialCharactersAndSpaces(binding.edtCode, binding.txtInform)


        /**-----------------------------------------------------------------------*/
        viewModelCoupon.isDuplicate.observe(this@CouponActivity) {
            if (it) {
                binding.txtInform.text =
                    "The coupon you just added seems to already exist!"
                binding.txtInform.show()
            } else {
                // Có cần set lại data cho adapter không nhỉ.
            }
        }


        binding.txtAddCoupon.setOnClickListener {
            binding.txtInform.hide()
            binding.edtCode.clearFocus()
            binding.edtDiscount.clearFocus()

            if (binding.edtCode.text.toString() != "" && binding.edtDiscount.text.toString() != "") {

                if (binding.edtCode.text.length >= 4) {
                    if (binding.edtDiscount.text.toString().toInt() != 0) {

                            viewModelCoupon.addCoupon(
                                this@CouponActivity,
                                CouponEntity(
                                    0,
                                    DateFormatUtil.getDateForCoupon(),
                                    binding.edtCode.text.toString(),
                                    binding.edtDiscount.text.toString().toInt(),
                                    1
                                )
                            )

                        binding.edtCode.text.clear()
                        binding.edtDiscount.text.clear()


                    } else {
                        binding.txtInform.text =
                            "The discount should be different from 0%."
                        binding.txtInform.show()
                    }


                } else {
                    binding.txtInform.text =
                        "The Coupon Code needs to consist of 4 to 8 characters!"
                    binding.txtInform.show()
                }


            } else {
                binding.txtInform.text = "The information above must not be empty!"
                binding.txtInform.show()
            }
        }
        /**-----------------------------------------------------------------------*/

        binding.txtClear.setOnClickListener {
            binding.edtCode.text.clear()
            binding.edtDiscount.text.clear()
        }

    }

    private fun backToManager() {
        openActivity(MainManagerActivity::class.java, true)
    }



    private fun showConfirmDialog(coupon: CouponEntity) {

        val build = AlertDialog.Builder(this@CouponActivity, R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_confirm_status_of_coupon, null)
        build.setView(view)


        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnRevert = view.findViewById<Button>(R.id.btnRevert)
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)


        imgClose.setOnClickListener { dialog.dismiss() }


        btnDone.setOnClickListener {
            coupon.coupon_status = 1
            viewModelCoupon.addCouponUpdate(coupon)
            dialog.dismiss()
        }

        btnRevert.setOnClickListener {
            dialog.dismiss()
        }
        // End. Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()

    }


    private fun showHideDialog(coupon: CouponEntity) {


        val build = AlertDialog.Builder(this@CouponActivity, R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_hide_coupon, null)
        build.setView(view)


        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnRevert = view.findViewById<Button>(R.id.btnRevert)
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)


        imgClose.setOnClickListener { dialog.dismiss() }


        btnDone.setOnClickListener {
            coupon.coupon_status = 2
            viewModelCoupon.addCouponUpdate(coupon)
            dialog.dismiss()
        }

        btnRevert.setOnClickListener {
            dialog.dismiss()
        }
        // End. Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }
}