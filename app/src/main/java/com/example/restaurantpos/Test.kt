//import com.example.restaurantpos.db.entity.ItemEntity
//
//viewModel.getItemByName(edtItemName.text.toString()).observe(viewLifecycleOwner) { listItem ->
//    if (listItem != null && listItem.isNotEmpty()) {
//        val existingItem = listItem[0]
//        if (edtItemName.text.trim().toString() == existingItem.item_name) {
//            txtInform.text = "This item already exists!"
//            txtInform.show()
//        } else {
//            viewModel.addCategoryItem(
//                ItemEntity(
//                    0,
//                    edtItemName.text?.trim().toString(),
//                    edtItemPrice.text?.trim().toString().toFloat(),
//                    itemImagePath,
//                    edtItemInventoryQuantity.text?.trim().toString().toInt(),
//                    category.category_id
//                )
//            )
//            dialog.dismiss()
//        }
//    }
//}
