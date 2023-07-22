package com.example.restaurantpos.db.entity


data class AccountShift(
    val account_shift_id: Int,

    val shift_id: String,

    val account_id: Int,

    val name: String)