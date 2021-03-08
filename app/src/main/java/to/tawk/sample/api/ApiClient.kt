package to.tawk.sample.api

import retrofit2.Response
import to.tawk.sample.data.User
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

     suspend fun getUsers(since: Int, pageSize:Int): Response<List<User>> {
        return apiService.getUser(since, pageSize)
    }

    suspend fun fetchProfile(userName: String): Response<User> {
        return apiService.fetchProfile(userName)
    }


}