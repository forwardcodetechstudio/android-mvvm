package to.tawk.sample.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import to.tawk.sample.data.User

interface ApiService {


    @GET("users")
    suspend fun getUser(@Query("since") lastUserId: Int): Response<List<User>>

}