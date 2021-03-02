package to.tawk.sample.api

import retrofit2.Response
import to.tawk.sample.data.User
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper {
    override suspend fun getUsers(since:Int): Response<List<User>> {
       return apiService.getUser(since)
    }
}