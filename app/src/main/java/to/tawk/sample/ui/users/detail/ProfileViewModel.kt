package to.tawk.sample.ui.users.detail

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import to.tawk.sample.data.User
import to.tawk.sample.repository.UsersRepository
import to.tawk.sample.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: UsersRepository): ViewModel() {

    val userName: MutableStateFlow<String> = MutableStateFlow("")

    val profileLiveData : LiveData<Resource<User>> = userName.asLiveData().switchMap {
        repository.fetchProfile(it).asLiveData()
    }
    var profile= MutableLiveData<User>()
    val notes = MutableLiveData<String>()

    fun saveNote()= repository.updateNote(getProfileId()!!, notes.value).asLiveData()

    private fun getProfileId() = profile.value?.id


}