package to.tawk.sample.repository


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import to.tawk.sample.api.ApiClient
import to.tawk.sample.data.User
import to.tawk.sample.di.UserDAO
import to.tawk.sample.room.UserDao
import to.tawk.sample.utils.Resource
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val apiClient: ApiClient,
    @UserDAO private val userDao: UserDao
) {

    fun getUsers(since: Int, pageSize: Int): Flow<Resource<List<User>>> {

        return flow {

            try {
                val source = userDao.getUsers(since, pageSize)

                // if there is not data in db for given conditions
                if (source.isEmpty()) {

                    emit(Resource.loading(null))
                    val resp = apiClient.getUsers(since)
                    userDao.insertAllUsers(resp.body()!!)
                    emit(Resource.success(resp.body()))

                } else
                    emit(Resource.success(source))
            } catch (ex: Exception) {
                emit(Resource.error(ex.message ?: "Something went wrong", null))
            }


        }.flowOn(Dispatchers.IO)

    }

    fun updateNote(userId: Int, noteText: String?): Flow<Resource<Boolean>> {
        return flow {

            try {
                userDao.updateNote(userId, noteText)

                emit(Resource.success(true))

            } catch (ex: Exception) {
                emit(Resource.error(ex.message ?: "Something went wrong", null))
            }

        }.flowOn(Dispatchers.IO)
    }

    fun fetchProfile(username: String): Flow<Resource<User>> {
        return flow {
            try {
                emit(Resource.loading(null))
                val resp = apiClient.fetchProfile(username)
                val user = resp.body()
                user?.let {
                    val userNote = userDao.getNote(user.id)
                    it.notes = userNote
                }
                emit(Resource.success(user))
            } catch (ex: Exception) {
                emit(Resource.error(ex.message ?: "Something went wrong", null))
            }

        }.flowOn(Dispatchers.IO)
    }
}