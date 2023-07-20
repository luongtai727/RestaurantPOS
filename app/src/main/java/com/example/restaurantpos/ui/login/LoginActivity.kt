package com.example.restaurantpos.ui.login

import android.view.LayoutInflater
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantpos.base.BaseActivity
import com.example.restaurantpos.databinding.ActivityLoginBinding
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var viewModel: LoginViewModel

    override fun initOnCreate() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        initListener()
    }

    private fun initListener() {

        DataUtil.setEditTextWithoutSpecialCharactersAndSpaces(binding.edtUsername, binding.txtInformLogin)
        DataUtil.setEditTextWithoutSpecialCharactersAndSpaces(binding.edtPassword, binding.txtInformLogin)

        binding.txtLogin.setOnClickListener {
            if (
                binding.edtUsername.text.toString().trim().isEmpty() ||
                binding.edtPassword.text.toString().trim().isEmpty()
            ) {


                CoroutineScope(Dispatchers.Main).launch {
                    showLoginInform()
                }


            } else {
                checkLogin(
                    binding.txtInformLogin,
                    binding.edtUsername.text.toString(),
                    DataUtil.convertToMD5(binding.edtPassword.text.toString())
                )
            }
        }
    }


    private fun showLoginInform() {
        binding.txtInformLogin.text = "Username & password must not be empty!"
        binding.txtInformLogin.show()
    }

    private fun checkLogin(txtInform: TextView, userName: String, password: String) {
        viewModel.checkLogin(this@LoginActivity,txtInform, userName, password)
    }

    // End
    override fun getInflaterViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}