package to.tawk.sample.ui.users.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import to.tawk.sample.data.User
import to.tawk.sample.repository.UsersRepository
import to.tawk.sample.utils.Resource
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(private val repository: UsersRepository): ViewModel() {
    private val originalUsersList = mutableListOf<User>()
    private val filteredUsersList = mutableListOf<User>()

    private var pageSize=0

    private val _users = MutableLiveData<Resource<List<User>>>()

    val users : LiveData<Resource<List<User>>>
        get() = _users


    init {
        getGitUsers()
    }

    private fun getGitUsers() = repository.getUsers(0)

}

