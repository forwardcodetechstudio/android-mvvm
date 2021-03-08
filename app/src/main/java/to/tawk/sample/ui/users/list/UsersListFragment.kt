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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import to.tawk.sample.R
import to.tawk.sample.databinding.FragmentUsersListBinding
import to.tawk.sample.utils.MyRecyclerViewScrollListener
import to.tawk.sample.utils.Status
import to.tawk.sample.utils.ViewExt.Companion.hideKeyboard
import to.tawk.sample.utils.ViewExt.Companion.showSnackBar

@AndroidEntryPoint
class UsersListFragment : Fragment(R.layout.fragment_users_list) {

    private val usersListViewModel: UsersListViewModel by viewModels()

    private lateinit var binding: FragmentUsersListBinding

    private val usersListAdapter by lazy {
        UsersListAdapter(requireContext()) {
            // navigate to detail
            val dir = UsersListFragmentDirections.actionUsersListFragmentToProfileFragment(it.login!!)
            findNavController().navigate(dir)
        }
    }


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

        usersListViewModel.users.observe(viewLifecycleOwner, {result->
            when(result.status){
                Status.LOADING->{
                    usersListViewModel.loading.value=true
                }
                Status.SUCCESS ->{
                    usersListViewModel.loading.value=false
                    result.data?.let{users->
                        usersListViewModel.addUsersToList(users)
                        usersListAdapter.addUsers(users)
                        usersListViewModel.pageSize=users.size // setting page size after first load
                    }

                }
                Status.ERROR->{
                    view?.showSnackBar(result.message)
                }
            }
        })
    }


    private fun setupUserList() {
        binding.usersList.apply {
            adapter = usersListAdapter
            addItemDecoration(DividerItemDecoration(this.context,DividerItemDecoration.VERTICAL))

            addOnScrollListener(object: MyRecyclerViewScrollListener(this.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                    if(!usersListViewModel.isInSearchMode){
                        showSnackBar("Loading more")
                        usersListViewModel.loadMore()
                    }
                }

            })
        }
    }

    private fun setupUserSearch() {
        binding.search.apply {

            // add on done press
            setOnEditorActionListener { _, actionId, _ ->
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    this.clearFocus()
                    hideKeyboard()
                    usersListViewModel.isInSearchMode=false
                }
                 true
            }

            // add text change listener
            addTextChangedListener {

                if(TextUtils.isEmpty(it?.toString())){
                    usersListAdapter.addUsers(usersListViewModel.getOriginalList())
                    usersListViewModel.isInSearchMode=false

                }
                else{
                    usersListViewModel.isInSearchMode=true
                    val filtered = usersListViewModel.getOriginalList()
                        .filter {user->
                            user.login!!.contains(it!!.toString()) || user.login== it.toString()
                        }

                    usersListAdapter.addUsers(filtered, true)
                }

            }
        }
    }
}