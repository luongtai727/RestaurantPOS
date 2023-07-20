package com.example.restaurantpos.network

// Nên để Long để tránh những trường hợp không đáng có như: Giá trị demo mình nhìn được nó quá bé ==> Mình hiểu nhầm ấy
data class WeatherItem(

    val id: Long,
    val main: String,
    val description: String,
    val icon: String
) {
}