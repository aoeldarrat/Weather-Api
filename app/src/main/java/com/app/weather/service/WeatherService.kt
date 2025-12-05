import com.app.weather.model.PointDataResponse
import com.app.weather.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("weather/{cityName}")
    suspend fun getWeather(
        @Path("cityName") cityName: String
    ): WeatherResponse

    @GET("points/{latitude},{longitude}")
    suspend fun getPointData(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): PointDataResponse
}

object ApiConfig {
    fun getApiService(): WeatherService {

        // API response interceptor
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        // Client
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weather.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(WeatherService::class.java)
    }
}