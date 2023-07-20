package com.example.restaurantpos.network

import com.intuit.sdp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// - - Retrofit2
// Thằng này được phát triển lên từ thằng okhttp3, nên vẫn phải import mớ đó vào
// Thằng okhttp3 lại phát triển từ thằng okhttp_request. Là thằng cơ bản nhất mà android phát triển lên
// Retrofit cũng như thằng Room vậy, đều phải tạo Model để hứng.
// Cách hứng:
// Bản thân trả về của Request là 1 Response, là 1 đống String
// Mình gọi là JSON thôi, nhưng JSON còn bao gồm nhiều thắng khác nữa
// Mình sẽ đi bóc tách thằng đó ra
// Ngoài cùng của response chỉ là { ==> Thể hiện thằng này chỉ là 1 Object
// => Xíu nữa hứng cũng là 1 class thôi. Chứ không phải là 1 list (Đặc điểm là bắt đầu từ [])
// Ví dụ: Chuyển sang xem dạng Giờ (hourly) --> Thì sẽ thấy dạng List liền

// Okay đi định nghĩa class để retrofit hứng được data thôi
// Đâu tiên đi định nghĩa phương thức Call ==> Đi định nghĩa class sau

// Truyền Params: Truyền lên theo dạng Key-Value
// Truyền Body: Truyền chính thằng Object (đã được mã hóa sang thằng JSON)
//              ==> Vậy nên mới bảo rằng, JSON hay đi với Call API
//              ==> Gói mang theo nên sẽ không hiển thị lên link ==> An toàn hơn truyền bằng params


// B1. Khai báo Server mà mình động đến: Là thằng Retrofit Client
//     object WeatherClient : Bên trong định nghĩa lại phương thức call thôi


// Truyền vào debug: Chế độ này được sinh ra để hỗ trợ những thằng API liên quan đến Doanh Nghiệp, và được kiểm soát.
// Nó ảnh hưởng đến doanh thu => Mình sẽ call ở chế độ Debug và Release. Góc dưới bên trái (Build Variants)
// Chuyển từ debug sang release --> Build ra là ảnh hưởng đến app đang release
// debug: Cắm cờ để người ta biết mình đang call chỉ là đang thử nghiệm ==> Hệ thống sẽ bỏ qua ==> Không tính vào những việc liên quan đến Doanh thu
// Ví dụ mình đang call liên quan đến quản cáo, mà để release thì thằng google sẽ kiểm duyệt, và có thể tính mình vào chế độ gian lận
// Có thể phạt. Kiểu add tài khoản quảng cáo, hủy bỏ doanh thu gần nhất
// debug, release, kiểu cờ để mình đánh dấu với tâng lớp bên trên
// Sau này đi làm sẽ là: BuildConfig.DEBUG   <== Định nghĩa trong Buil Gradle. Mình không có nên đang đặt vào bằng tay "debug"
// BuildConfig là 1 file sinh ra trong quá trình mình chạy, kiểm soát chế độ của mình
// Đi làm là phải thêm dòng trên vào chứ call API lung tung để rồi công ty bị đấm, thì mình cũng ăn Đấm theo ấy


//    by lazy: Bình thường 1 biến không dùng đến, nhưng vẫn khai báo
//             ==> Tốn tài nguyên
//             ==> by lazy sinh ra ==> Lần đầu tiên tạo thằng biến đấy thì nó mới được khai báo
//    Những lần sau thì nó không gọi lại nữa. Lúc này mặc định đã được khai báo rồi
//    Đơn giản: Nếu không dùng đến thì không bị tốn tài nguyên
object WeatherRetrofitClient {

    // Tạo domain. Domain này + @GET("data/2.5/weather") = 1 URL
    // Dấu / có thể thiếu, nó sẽ biết để tự thêm
    private const val BASE_URL = "https://api.openweathermap.org/"


    /**  1. Khai báo phương thức  */
    private val retrofitClient: Retrofit.Builder by lazy {

        val levelType: HttpLoggingInterceptor.Level =
            if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
                HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())

    }
    // Thực chất không có cái này nó vẫn chạy. Cơ mà google đưa ra thế thì cứ theo thôi

    /**  2. Khai báo biến truy cập (Interface: Cũng giống như thằng DAO thôi)  */
// Chóc nữa dùng thằng Interface này để giao tiếp

    val weatherAPIInterface: WeatherAPIInterface by lazy {
        retrofitClient.build().create(WeatherAPIInterface::class.java)
    }


}