package to.tawk.sample.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import to.tawk.sample.data.User

interface ApiService {


    @GET("users/since={last_user_id}")
    suspend fun getUser(@Path("last_user_id") lastUserId: Int): Response<List<User>>

}