package com.example.restaurantpos.ui.manager.category

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentManagerCategoryComponentBinding
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.util.RealPathUtil
import com.example.restaurantpos.util.hide
import com.example.restaurantpos.util.show
import com.example.restaurantpos.util.showToast
import java.io.IOException
import kotlin.math.round

/**
 * Truyền vào position: Int để chuyển tab
 * Truyền vào category: CategoryEntity để get ra CategoryComponent
 */
class ManagerCategoryComponentFragment(position: Int, var category: CategoryEntity) : Fragment(),
    ManagerCategoryComponentAdapter.EventClickItemCategoryListener {

    private lateinit var binding: FragmentManagerCategoryComponentBinding
    private lateinit var adapter: ManagerCategoryComponentAdapter
    private lateinit var viewModel: CategoryViewModel
    lateinit var dialog: AlertDialog
    private var itemImagePath = ""

    private var selectedImagePath: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagerCategoryComponentBinding.inflate(inflater, container, false)

        adapter = ManagerCategoryComponentAdapter(requireContext(), mutableListOf(), this)

        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        binding.rcyCategoryComponentManagement.adapter = adapter

        return binding.root
    }


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Chuc nang Add Category ClipData.Item */
        binding.imgAddCategoryItem.setOnClickListener {
            showAddCategoryItemDialog()
        }

        viewModel.getListCategoryComponentItem(category.category_id).observe(viewLifecycleOwner) {
            adapter.setListData(it)
        }

    }

    private fun showMessage(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("SetTextI18n")
    private fun showAddCategoryItemDialog() {
        itemImagePath = ""
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_add_category_item, null)
        build.setView(view)

        // 1.  Get Component of Dialog
        val txtInform = view.findViewById<TextView>(R.id.txtInform)
        val edtItemName = view.findViewById<EditText>(R.id.edtItemName)
        val edtItemPrice = view.findViewById<EditText>(R.id.edtItemPrice)
        val edtItemInventoryQuantity = view.findViewById<EditText>(R.id.edtItemInventoryQuantity)

        val imgShow = view.findViewById<ImageView>(R.id.imgShow)
        val btnChoseImage = view.findViewById<Button>(R.id.btnChoseImage)
        val btnAddItem = view.findViewById<Button>(R.id.btnAddItem)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        val imgClose = view.findViewById<ImageView>(R.id.imgCloseDialogAddItem)

        // 2.  Code cho dau X & Cancel Button
        imgClose.setOnClickListener { dialog.dismiss() }
        btnCancel.setOnClickListener { dialog.dismiss() }

        // 3.  Code for ChooseImage
        btnChoseImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Photo Quang"), 101)
        }


        // 4.  Code cho AddItem Button

        viewModel.isDuplicate.observe(viewLifecycleOwner) {
            if (it) {

                txtInform.text = "This item's name may already exist \n in your category!"
                txtInform.show()

//                showMessage("Duplication")
            }
            else dialog.dismiss()
        }

        btnAddItem.setOnClickListener {
            /**===========================================================*/
            txtInform.hide()

            if (edtItemName.text.toString() != ""
                && edtItemPrice.text.toString() != ""
                && edtItemInventoryQuantity.text.toString() != ""
            ) {


//                val listItem = viewModel.getItemByName(edtItemName.text.toString())
//
//                if (listItem.isEmpty()) {
//                    if (listItem[0].item_name == edtItemName.text.toString()) {
//                        txtInform.text = "This item already exists!"
//                        txtInform.show()
//                    } else {
                viewModel.addCategoryItem(
                    ItemEntity(
                        0,
                        edtItemName?.text.toString(),
                        edtItemPrice?.text.toString().toFloat(),
                        itemImagePath,
                        edtItemInventoryQuantity?.text.toString().toInt(),
                        category.category_id
                    )
                )
//                dialog.dismiss()
            } else {
//                }

//        } else {
                txtInform.text = "Information below must not be empty!"
                txtInform.show()
            }


            /**===========================================================*/
        }

        // End. Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }

    override fun clickUpdateItem(itemCategory: ItemEntity) {
        showChangeItemDialog(itemCategory)
    }

    override fun longClickDeleteItem(itemCategory: ItemEntity) {
        showDeleteItemDialog(itemCategory)
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    private fun showChangeItemDialog(itemOfCategory: ItemEntity) {
        selectedImagePath = ""
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_change_category_item, null)
        build.setView(view)

        // 1.  Get Component of Dialog
        val edtItemName = view.findViewById<EditText>(R.id.edtItemName)
        val edtItemPrice = view.findViewById<EditText>(R.id.edtItemPrice)
        val edtItemInventoryQuantity = view.findViewById<EditText>(R.id.edtItemInventoryQuantity)

        val imgShow = view.findViewById<ImageView>(R.id.imgShow)
        val btnChoseImage = view.findViewById<Button>(R.id.btnChoseImage)
        val btnUpdateItem = view.findViewById<Button>(R.id.btnUpdateItem)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        val imgClose = view.findViewById<ImageView>(R.id.imgCloseDialogAddItem)

        val txtUpdateItem = view.findViewById<TextView>(R.id.txtUpdateItem)

        // 2.  Code cho dau X & Cancel Button
        imgClose.setOnClickListener { dialog.dismiss() }
        btnCancel.setOnClickListener { dialog.dismiss() }
        txtUpdateItem.text = "Update " + itemOfCategory.item_name

        edtItemName.hint = itemOfCategory.item_name
        edtItemPrice.hint = round(itemOfCategory.price).toString()
        edtItemInventoryQuantity.hint = itemOfCategory.inventory_quantity.toString()
        imgShow.setImageBitmap(BitmapFactory.decodeFile(itemOfCategory.image))

        // 3.  Code for ChooseImage
        btnChoseImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Photo Quang 1"), 102)
        }
        // 4.  Code cho AddItem Button
        btnUpdateItem.setOnClickListener {
            if (edtItemName.text.toString() != "") {
                itemOfCategory.item_name = edtItemName.text.toString()
            }
            if (edtItemPrice.text.toString() != "") {
                itemOfCategory.price = edtItemPrice.text.toString().toFloat()
            }
            if (edtItemInventoryQuantity.text.toString() != "") {
                itemOfCategory.inventory_quantity = edtItemInventoryQuantity.text.toString().toInt()
            }
            if (selectedImagePath != "") {
                itemOfCategory.image = selectedImagePath as String
            }
            viewModel.addCategoryItem(itemOfCategory)
            dialog.dismiss()
        }

        // End. Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDeleteItemDialog(itemOfCategory: ItemEntity) {
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_delete_category_item, null)
        build.setView(view)


        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        val txtDeleteItem = view.findViewById<TextView>(R.id.txtDeleteItem)


        imgClose.setOnClickListener { dialog.dismiss() }
        btnNo.setOnClickListener { dialog.dismiss() }
        txtDeleteItem.text = "Delete " + itemOfCategory.item_name + "?"


        btnYes.setOnClickListener {
            viewModel.deleteItemOfCategory(itemOfCategory)
            dialog.dismiss()
        }

        // End. Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataIntent)
        /** Choose Image when Adding */
        if (requestCode == 101 && resultCode == AppCompatActivity.RESULT_OK) {
            if ((dataIntent != null) && (dataIntent.data != null)) {
                try {
                    dialog.findViewById<ImageView>(R.id.imgShow)?.setImageBitmap(
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            dataIntent.data   // URI cung cấp cho bên dưới
                        )
                    )
                    itemImagePath = RealPathUtil.getRealPath(requireContext(), dataIntent.data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                requireContext().showToast("Error")
            }
        }

        /** Choose Image when Updating */
        if (requestCode == 102 && resultCode == AppCompatActivity.RESULT_OK) {
            if ((dataIntent != null) && (dataIntent.data != null)) {
                try {
                    dialog.findViewById<ImageView>(R.id.imgShow)?.setImageBitmap(
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            dataIntent.data   // URI cung cấp cho bên dưới
                        )
                    )
                    selectedImagePath = RealPathUtil.getRealPath(requireContext(), dataIntent.data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                requireContext().showToast("Error")
            }
        }
    }

}
/** Hàm này chưa hiểu rõ */

//    Kết nối với   // 6.  Code cho ADD Button

//    Bình thường khi 1 Activity nó kết thúc
//    Bình thường chỉ thấy truyền dữ liệu từ màn này sang màn khác để chuyển màn
//    Còn 1 sự kiện nữa là lúc nó kết thúc nó có thể đáp data cho bất kì màn nào mà trước đấy được quay trở lại
//    Line 93: intent.action = Intent.ACTION_GET_CONTENT
//    Hệ thống có rất nhiều action: Có cả action setting luôn.
//    Acction của mình lấy content là mình chọn dữ liệu của máy
//    Lúc này Nếu không có  intent.type = "image/*"
//    thì nó sẽ mở hẳn bộ nhớ máy ra cho mình luôn
//    Mình có thể thay bằng intent.type = "*/*" (Kiểu/loại)-> Sẽ hiển thị tất cả những gì mà mình có thể lấy được
//    * Tất cả những file: video (mp4, mp3,...), image, GIF
//    image/*  --> * là JPG, JPEG,PNG, GIF,...

//    onActivityResult  --> Là 1 màn của Activity luôn.
//    Đoạn code bên dưới không phải do thằng // 6.  Code cho ADD Button trả ra đâu
//    Mình có thể tạo ra sự kiện onActivityResult. Nó là hàm chuyển ấy
//    Trước khi kết thúc mình chỉ cần gọi ấy thôi là nó sẽ tự chuyển ra
//    Lo Search Google đi chứ!. Nó có tính phí đâu (create, get, how to  ... android studio)
//    Đọc của bọn android thường khó hiểu lắm. Chọn StackOverFlow, để ý những link nhỏ bên dưới!
//
//    //6:  startActivityForResult(Intent.createChooser(intent, "Select Photo Quang"), 101)
//    onActivityResult --> Hứng lại luôn luôn theo cặp:
//    RequestCode: Cái mình gọi ra để đánh dấu. Nhiều thằng lắm nên cần RequestCode để phân biệt
//    Số RequestCode là do mình tạo ra để đánh dấu sự kiện startActivityForResult của mình
//    Vì mỗi thời điểm nó có thể trả ra rất nhiều màn hình được trả về
//    resultCode == AppCompatActivity.RESULT_OK là cái trả ra của hệ thống, cứ = OK là được
//    Chọc vào trong hàm mà xem
//
//    Mình chia if
//        1. Là của mình (Xe của mình và thùng xe chứa data, thì mình mới xử lý tiếp) và nó có tha data về đã

