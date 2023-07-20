package com.example.restaurantpos.ui.manager.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val _isDuplicate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isDuplicate: LiveData<Boolean> = _isDuplicate


    fun addCategory(data: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addCategory(data)
        }
    }


  /*  fun addCategoryItem(categoryItem: ItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addCategoryItem(categoryItem)
        }
    }*/

    fun addCategoryItem(categoryItem: ItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {

            val existingAccountShift =  DatabaseUtil.getItemByName(categoryItem.item_name)

            if (existingAccountShift.isEmpty()) {
                // Nếu tài khoản chưa tồn tại, thêm vào cơ sở dữ liệu
                DatabaseUtil.addCategoryItem(categoryItem)
                _isDuplicate.postValue(false)
            } else {
                // Nếu tài khoản đã tồn tại, xử lý tương ứng (ví dụ: hiển thị thông báo lỗi)
                _isDuplicate.postValue(true)
            }

        }
    }

    fun deleteItemOfCategory(itemOfCategory: ItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.deleteItemOfCategory(itemOfCategory)
        }
    }

    fun addListCategoryItem(listCategoryItem: List<ItemEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.addListCategoryItem(listCategoryItem)
        }
    }


    fun getAllCategory() = DatabaseUtil.getAllCategory()
    fun getItemOfCategory(item_id: Int) = DatabaseUtil.getItemOfCategory(item_id)

    fun getListCategoryComponentItem(categoryComponentId: Int) =
        DatabaseUtil.getListCategoryComponentItem(categoryComponentId)


//    lateinit var listItemByName: List<ItemEntity>
//
//    fun getItemByName(name: String) : List<ItemEntity> {
//        viewModelScope.launch(Dispatchers.IO) {
//            listItemByName =  DatabaseUtil.getItemByName(name)
//            return listItemByName
//        }
//    }



}