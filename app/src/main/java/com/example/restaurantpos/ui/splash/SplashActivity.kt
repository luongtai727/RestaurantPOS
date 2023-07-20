package com.example.restaurantpos.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantpos.base.BaseActivity
import com.example.restaurantpos.databinding.ActivitySplashBinding
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountRoleEntity
import com.example.restaurantpos.db.entity.AccountStatusEntity
import com.example.restaurantpos.db.entity.CartItemStatusEntity
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.db.entity.CouponEntity
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.db.entity.OrderStatusEntity
import com.example.restaurantpos.db.entity.ShiftEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.db.entity.TableStatusEntity
import com.example.restaurantpos.ui.login.LoginActivity
import com.example.restaurantpos.util.DataUtil


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    lateinit var viewModel: SplashViewModel
    private val DELAY_TIME = 1500L


    override fun initOnCreate() {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        /** Set Data for DB */
        createDataFirst()
        /** Start Login Screen */
        startLoginActivity()
    }

    private fun createDataFirst() {
        /** 1. Account */
        viewModel.addListAccount(
            listOf(
                AccountEntity(
                    1,
                    "Quang Manager",
                    "1995/03/02",
                    "08010802100",
                    "quang0",
                    DataUtil.convertToMD5("123"),
                    0,
                    true
                ),
                AccountEntity(
                    2,
                    "Quang Receptionist",
                    "1995/03/02",
                    "080101232100",
                    "quang1",
                    DataUtil.convertToMD5("123"),
                    1,
                    true
                ),
                AccountEntity(
                    3,
                    "Quang Kitchen",
                    "1995/03/02",
                    "08010531100",
                    "quang2",
                    DataUtil.convertToMD5("123"),
                    2,
                    true
                ),
            )
        )
        /** 1.1. Account_Role */
        viewModel.addListAccountRole(
            listOf(
                AccountRoleEntity(0, "Manager"),
                AccountRoleEntity(1, "Receptionist"),
                AccountRoleEntity(2, "Kitchen")
            )
        )
        /** 1.2. Account_Status */
        viewModel.addListAccountStatus(
            listOf(
                AccountStatusEntity(false, "Inactive"),
                AccountStatusEntity(true, "Active")
            )
        )

        /** 2. Table */
        viewModel.addTable(this, TableEntity(1, "TAKE AWAY", 4, 0))
        viewModel.addTable(this, TableEntity(2, "Table 01", 4, 0))
        viewModel.addTable(this, TableEntity(3, "Table 02", 4, 0))
        viewModel.addTable(this, TableEntity(4, "Table 03", 4, 0))
        viewModel.addTable(this, TableEntity(5, "Table 04", 4, 0))
        viewModel.addTable(this, TableEntity(6, "Table 05", 4, 0))
        viewModel.addTable(this, TableEntity(7, "Table 06", 4, 0))
        viewModel.addTable(this, TableEntity(8, "Table 07", 4, 0))
        viewModel.addTable(this, TableEntity(9, "Table 08", 4, 0))
        viewModel.addTable(this, TableEntity(10, "Table 09", 4, 0))
        viewModel.addTable(this, TableEntity(11, "Table 10", 4, 0))
        viewModel.addTable(this, TableEntity(12, "Table 11", 4, 0))

        /** 2.1. Table_Status */
        viewModel.addListTableStatus(
            listOf(
                TableStatusEntity(0, "Empty"),
                TableStatusEntity(1, "In New Order"),
                TableStatusEntity(2, "In Old Order"),
                TableStatusEntity(3, "Problem")
            )
        )

        /** 3. Category */
        viewModel.addCategory(CategoryEntity(1, "FOODS"))
        viewModel.addCategory(CategoryEntity(2, "DRINKS"))
        viewModel.addCategory(CategoryEntity(3, "DESSERTS"))

        /** 4. Item */
        viewModel.addCategoryItem(ItemEntity(0, "Mon Nhau 1", 131.1f, "", 5, 1))
        viewModel.addCategoryItem(ItemEntity(0, "Mon Nhau 2", 1111.1f, "", 1, 2))
        viewModel.addCategoryItem(ItemEntity(0, "Mon Nhau 3", 22.1f, "", 3, 3))
        viewModel.addCategoryItem(ItemEntity(0, "Mon Nhau 5", 13.1f, "", 5, 1))
        viewModel.addCategoryItem(ItemEntity(0, "Mon Nhau 6", 15.1f, "", 1, 2))
        viewModel.addCategoryItem(ItemEntity(0, "Mon Nhau 7", 111.1f, "", 3, 3))

        /** 4.1. Cart_Item_Status */
        viewModel.addListCartItemStatus(
            listOf(
                CartItemStatusEntity(0, "Waiting"),
                CartItemStatusEntity(1, "In Progress"),
                CartItemStatusEntity(2, "Done"),
                CartItemStatusEntity(3, "Served")
            )
        )

        /** 4.2. Order_Status */
        viewModel.addListOrderStatus(
            listOf(
                OrderStatusEntity(0, "After clicking the table"),
                OrderStatusEntity(1, "After Ordering"),
                OrderStatusEntity(2, "After Checkout")
            )
        )

        /** 5. Customer  */
        viewModel.addCustomer(CustomerEntity(1, "Quang 1", "08034931491", "1995/03/02", 0.0))
        viewModel.addCustomer(CustomerEntity(2, "Quang 2", "09034931492", "1995/03/03", 3000.0))
        viewModel.addCustomer(CustomerEntity(3, "Quang 3", "07034931493", "1995/03/04", 11000.0))

        /** 6. Shift  */

        viewModel.addListShift(
            listOf(
                ShiftEntity("1", "Morning", "8:00", "12:00"),
                ShiftEntity("2", "Afternoon", "12:00", "18:00"),
                ShiftEntity("3", "Night", "18:00", "22:00")
            )
        )


        /** 7. Coupon  */

        viewModel.addCoupon(CouponEntity(1, "2023/07/10", "QUANG1", 10, 1))
        viewModel.addCoupon(CouponEntity(2, "2023/07/11", "QUANG2", 21, 0))
        viewModel.addCoupon(CouponEntity(3, "2023/07/12", "QUANG3", 32, 1))
        viewModel.addCoupon(CouponEntity(4, "2023/07/13", "QUANG4", 43, 0))
        viewModel.addCoupon(CouponEntity(5, "2023/07/14", "QUANG5", 54, 1))
    }

    private fun startLoginActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }, DELAY_TIME)
    }

    override fun getInflaterViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}