package com.example.restaurantpos.ui.adapter

/** Add Accouont_Shift Dialog */
// Copy từ NewOrderFragment sang
/*
private fun showAddAccountShiftDialog(shift_id: String) {
// -----------------Prepare--------------------------------------------------//
    // 1.  Build Dialog
    // 2.  Designed XML --> View
    // 3.  Set VIEW tra ve above --> Dialog
    val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
    val view = layoutInflater.inflate(R.layout.dialog_alert_add_account_shift, null)
    build.setView(view)
    // 4.  Get Component of Dialog
    val edtShiftYear = view.findViewById<TextView>(R.id.edtShiftYear)
    val edtShiftMonth = view.findViewById<TextView>(R.id.edtShiftMonth)
    val edtShiftDay = view.findViewById<TextView>(R.id.edtShiftDay)
    val edtShiftName = view.findViewById<TextView>(R.id.edtShiftName)
    val edtStaffName = view.findViewById<EditText>(R.id.edtStaffName)

    val rcyStaffSelection = view.findViewById<RecyclerView>(R.id.rcyStaffSelection)

    val btnAddStaffShift = view.findViewById<Button>(R.id.btnAddStaffShift)
    val btnCancel = view.findViewById<Button>(R.id.btnCancel)
    val imgClose = view.findViewById<ImageView>(R.id.imgCloseDialogCustomer)
    // -----------------Code for Component----------------------------------------//
      // rcyStaffSelection
      adapterStaffSelection = StaffSelectionAdapter(requireParentFragment(), ArrayList(), object :
          StaffSelectionAdapter.EventClickStaffListener {
          override fun clickStaff(itemStaff: AccountEntity) {
              staffObject = itemStaff

          }
      })

      rcyStaffSelection.adapter = adapterStaffSelection

      // 2. Code for when staff types on edtPhoneNumber and contain >= 3 Chars. If exist --> Show for Picking-up
      // SetData for (1)
      edtStaffName.doOnTextChanged { text, start, before, count ->
          if (text.toString().isNotEmpty()) {
              viewModelUser.getStaffByName(text.toString())
                  .observe(viewLifecycleOwner) { staffByName ->
                      if (staffByName.size > 0) {
                          adapterStaffSelection.setListData(staffByName as ArrayList<AccountEntity>)
                          rcyStaffSelection.show()
                      }
                  }
          } else {
              rcyStaffSelection.gone()
          }
      }


      val shiftID = DateFormatUtil.getShiftId(
          edtShiftYear.text.toString().toInt(),
          edtShiftMonth.text.toString().toInt(),
          edtShiftDay.text.toString().toInt(),
          edtShiftName.text.toString().toInt()
      )

      // 4.  Add AccountShift
      btnAddStaffShift.setOnClickListener {
          viewModelShift.addAccountShift(
              AccountShiftEntity(
                  0, shiftID, staffObject!!.account_id
              )
          )
                      viewModelShift.getListAccountShiftForSetListData(shiftID).observe(viewLifecycleOwner) {
                            adapterShift.setListData(it)
                        }
        }

    // Other:  Dau X  &   Cancel Button
    imgClose.setOnClickListener { dialog.dismiss() }
    btnCancel.setOnClickListener { dialog.dismiss() }

    // End: Tao Dialog (Khi khai bao chua thuc hien) and Show len display
    dialog = build.create()
    dialog.show()
}*/
