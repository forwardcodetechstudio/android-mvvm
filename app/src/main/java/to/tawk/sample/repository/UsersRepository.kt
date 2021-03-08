package to.tawk.sample.repository


import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import to.tawk.sample.api.ApiClient
import to.tawk.sample.data.User
import to.tawk.sample.di.UserDAO
import to.tawk.sample.room.UserDao
import to.tawk.sample.utils.Resource
import to.tawk.sample.utils.ViewExt.Companion.isNetworkAvailable
import to.tawk.sample.workmanager.RequestQueueWorker
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val apiClient: ApiClient,
    @UserDAO private val userDao: UserDao,
    @ApplicationContext private val appContext: Context
) {

    /**
     * Get users list
     */
    fun getUsers(since: Int, pageSize: Int): Flow<Resource<List<User>>> {


        return flow {

                val source = userDao.getUsers(since)

                // if there is not data in db for given conditions
                if (source.isEmpty()) {
                    emit(Resource.loading(null))
                    val resp = apiClient.getUsers(since, pageSize)
                    userDao.insertAllUsers(resp.body()!!)
                    emit(Resource.success(resp.body()))
                } else
                    emit(Resource.success(source))
        }
            .flowOn(Dispatchers.IO)
            .catch {ex->
                if(ex is UnknownHostException){

                    LocalBroadcastManager.getInstance(appContext)
                        .sendBroadcast(Intent("no-internet").putExtra("available",false))

                    val input = workDataOf("pageSize" to pageSize, "since" to since )
                    val constraints= Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    //schedule work manager
                    val uploadWorkRequest: WorkRequest =
                        OneTimeWorkRequestBuilder<RequestQueueWorker>()
                            .setConstraints(constraints)
                            .setInputData(input)
                            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 20, TimeUnit.SECONDS)
                            .build()

                    WorkManager
                        .getInstance(appContext)
                        .enqueue(uploadWorkRequest)
                }
                emit(Resource.error(ex.message ?: "Something went wrong", null))

            }

    }

    /**
     * Update note to local db
     */
    fun updateNote(userId: Int, noteText: String?): Flow<Resource<Boolean>> {
        return flow {

                userDao.updateNote(userId, noteText)

                emit(Resource.success(true))


        }.flowOn(Dispatchers.IO)
            .catch {ex->
                emit(Resource.error(ex.message ?: "Something went wrong", null))

            }
    }

    fun fetchProfile(username: String): Flow<Resource<User>> {
        return flow {
                emit(Resource.loading(null))
                val resp = apiClient.fetchProfile(username)
                val user = resp.body()
                user?.let {
                    val userNote = userDao.getNote(user.id)
                    it.notes = userNote
                }
                emit(Resource.success(user))

        }.flowOn(Dispatchers.IO)
            .catch {ex->
                emit(Resource.error(ex.message ?: "Something went wrong", null))

            }
    }
}