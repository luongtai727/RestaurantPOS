package com.example.restaurantpos.db.entity

data class ItemRevenue(
    val item_id: Int,
    val item_name: String,
    val price: Float,
    val order_quantity: Int,
    val order_id: String,

)