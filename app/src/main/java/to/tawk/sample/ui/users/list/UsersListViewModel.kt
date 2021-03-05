package to.tawk.sample.ui.users.list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

import to.tawk.sample.data.User
import to.tawk.sample.repository.UsersRepository
import to.tawk.sample.utils.Resource
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(private val repository: UsersRepository): ViewModel() {

    private val originalUsersList = mutableListOf<User>()
    val loading=MutableLiveData(false)
    private val since: MutableStateFlow<Int> = MutableStateFlow(0)
    var pageSize=30

    var isInSearchMode=false




    val users : LiveData<Resource<List<User>>> = since.asLiveData().switchMap {
        repository.getUsers(it,pageSize).asLiveData()
    }


    fun addUsersToList(list: List<User>){
        originalUsersList.clear()
        originalUsersList.addAll(list)
    }

    fun getOriginalList() = originalUsersList

    fun loadMore(){
        since.value = originalUsersList[originalUsersList.size-1].id
    }

}

