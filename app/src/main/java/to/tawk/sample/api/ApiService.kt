package to.tawk.sample.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import to.tawk.sample.data.User
import java.util.concurrent.TimeUnit

interface ApiService {


    @GET("users/since={last_user_id}")
    suspend fun getUser(@Path("last_user_id") lastUserId: Int): Response<List<User>>

}