package to.tawk.sample.ui.users.list

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import to.tawk.sample.R
import to.tawk.sample.databinding.FragmentUsersListBinding
import to.tawk.sample.utils.Status
import to.tawk.sample.utils.ViewExt.Companion.hideKeyboard

@AndroidEntryPoint
class UsersListFragment : Fragment(R.layout.fragment_users_list) {

    private val usersListViewModel: UsersListViewModel by viewModels()
    private val usersListAdapter by lazy {
        UsersListAdapter(requireContext())
    }

    private lateinit var binding: FragmentUsersListBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersListBinding.bind(view)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel= usersListViewModel

        setupUserList()
        setupUserSearch()
        observeUserList()
    }

    private fun observeUserList() {
        usersListViewModel.users.observe(viewLifecycleOwner, {
            when(it.status){
                Status.LOADING->{
                    usersListViewModel.loading.value=true
                }
                Status.SUCCESS ->{
                    usersListViewModel.loading.value=false
                    it.data?.let{users->
                        usersListViewModel.addUsersToList(users)
                        usersListAdapter.addUsers(users)
                    }

                }
                Status.ERROR->{

                }
            }
        })
    }


    private fun setupUserList() {
        binding.usersList.apply {
            adapter = usersListAdapter
            addItemDecoration(DividerItemDecoration(this.context,DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupUserSearch() {
        binding.search.apply {

            // add on done press
            setOnEditorActionListener { _, actionId, _ ->
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    this.clearFocus()
                    hideKeyboard()
                }
                 true
            }


            // add text change listener
            addTextChangedListener {

                if(TextUtils.isEmpty(it?.toString())){
                    usersListAdapter.addUsers(usersListViewModel.getOriginalList())
                }
                else{
                    val filtered = usersListViewModel.getOriginalList()
                        .filter {user->
                            user.login!!.contains(it!!.toString())
                        }

                    usersListAdapter.addUsers(filtered)
                }

            }
        }
    }
}