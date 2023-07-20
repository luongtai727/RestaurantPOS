package com.example.restaurantpos.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Get: Domain, và những source ở đằng sau (Địa chỉ truy cập) ==> Đưa những thứ phía sau vào get
// Còn lại bên trong phương thức, hãy truyền vào đúng những gì mà nó yêu cầu. Mọi thứ bắt đầu từ sau dấu ?
// Mình ở dạng get (Params) --> Chỉ có thể là Query thôi

// Call: Nhớ implement của thằng Retrofit 2 nhé. Trường hợp không có mới dùng lại của thằng Cha thôi (okhttp3)
// Response: Trả ra cả cục luôn ==> Tạo 1 Object để chứa  ==> Đi bốc tách ra cái muốn dùng

// Bốc tách xong thì dùng cái mình cần thôi ==> WeatherAPIDataModel
interface WeatherAPIInterface {


    @GET("data/2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Call<WeatherResponse>
}