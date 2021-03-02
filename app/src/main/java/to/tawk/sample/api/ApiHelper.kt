package to.tawk.sample.api

import retrofit2.Response
import to.tawk.sample.data.User

interface ApiHelper {

    suspend fun getUsers(since:Int): Response<List<User>>
}