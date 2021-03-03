package to.tawk.sample.api

import retrofit2.Response
import to.tawk.sample.data.User
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

     suspend fun getUsers(since: Int): Response<List<User>> {
        return apiService.getUser(since)
    }


}