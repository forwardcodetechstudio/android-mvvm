package to.tawk.sample.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import to.tawk.sample.api.ApiHelper
import to.tawk.sample.data.User
import to.tawk.sample.room.UserDao
import to.tawk.sample.utils.Resource
import javax.inject.Inject

class UsersRepository @Inject constructor(private val apiHelper: ApiHelper, private val userDao:UserDao) {

     fun getUsers(since:Int): LiveData<Resource<List<User>>> {

       return liveData(Dispatchers.IO) {
           emit(Resource.loading(null))

           val source = userDao.getUsers()
           emit(Resource.success(source))

           val resp = apiHelper.getUsers(since)
           userDao.insertAllUsers(resp.body()!!)

       }

    }
}