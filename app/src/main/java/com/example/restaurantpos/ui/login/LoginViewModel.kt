package com.example.restaurantpos.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.ui.main.MainManagerActivity
import com.example.restaurantpos.ui.main.MainReceptionistActivity
import com.example.restaurantpos.util.DatabaseUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.openActivity
import com.example.restaurantpos.util.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    //    private val view: MutableLiveData<View> = MutableLiveData()

    @SuppressLint("StaticFieldLeak")
    fun checkLogin(context: Context, txtInform: TextView, userName: String, password: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val loginAccountList = DatabaseUtil.checkLogin(userName, password)

            if (loginAccountList.isEmpty())
            {
                CoroutineScope(Dispatchers.Main).launch {
                    txtInform.text = "Username or password is wrong!"
                    txtInform.show()
                }

            }
            else
            {
                val acc = loginAccountList[0]

                if (!acc.account_status_id){

                    CoroutineScope(Dispatchers.Main).launch {
                        txtInform.text = "Your account was disabled!"
                        txtInform.show()
                    }

                }else{
                    when (acc.role_id) {
                        0 -> {
                            /*                       SharedPreferencesUtils.setUserName(acc.user_name)
                            SharedPreferencesUtils.setPassword(acc.password)*/
                            SharedPreferencesUtils.setAccountName(acc.account_name)
                            SharedPreferencesUtils.setAccountId(acc.account_id)
                            SharedPreferencesUtils.setAccountRole(acc.role_id)
                            context.openActivity(MainManagerActivity::class.java)
                        }

                        1 -> {
                            SharedPreferencesUtils.setAccountName(acc.account_name)
                            SharedPreferencesUtils.setAccountId(acc.account_id)
                            SharedPreferencesUtils.setAccountRole(acc.role_id)
                            context.openActivity(
                                MainReceptionistActivity::class.java,
                                bundleOf("NavigateByRole" to acc.role_id)
                            )
                        }

                        2 -> {
                            SharedPreferencesUtils.setAccountName(acc.account_name)
                            SharedPreferencesUtils.setAccountId(acc.account_id)
                            SharedPreferencesUtils.setAccountRole(acc.role_id)
                            context.openActivity(
                                MainReceptionistActivity::class.java,
                                bundleOf("NavigateByRole" to acc.role_id)
                            )
                        }
                    }
                }
            }
        }
//}

//    fun addToken(data: TokenEntity) {
//        CoroutineScope(Dispatchers.IO).launch {
//            DatabaseUtil.addToken(data)
//        }
//    }
//
//    fun checkToken(token: String, now: String) = DatabaseUtil.checkToken(token, now)
    }
}
