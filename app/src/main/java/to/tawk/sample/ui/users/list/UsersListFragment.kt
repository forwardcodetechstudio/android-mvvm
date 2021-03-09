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
import androidx.recyclerview.widget.ConcatAdapter
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

    private val loadingAdapter by lazy {
        LoaderAdapter()
    }

    private val usersAdapter by lazy {
        ConcatAdapter()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersListBinding.bind(view)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel= usersListViewModel

        setupUserList()
        setupUserSearch()
        observeUserList()


        usersListViewModel.loadingMore.observe(viewLifecycleOwner, {
            if(it)
            usersAdapter.addAdapter(loadingAdapter)
        })
    }


    private fun observeUserList() {


        usersListViewModel.users.observe(viewLifecycleOwner, {result->
            when(result.status){
                Status.LOADING->{
                    binding.shimmerLayout.apply {
                        showShimmer(true)
                    }

                }

                Status.SUCCESS ->{
                    binding.shimmerLayout.apply {
                        visibility= View.GONE
                    }

                    if(usersAdapter.adapters.size>1){
                        usersAdapter.removeAdapter(loadingAdapter)
                    }


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
        usersAdapter.addAdapter(usersListAdapter)

        binding.usersList.apply {
            adapter = usersAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context,DividerItemDecoration.VERTICAL))

            addOnScrollListener(object: MyRecyclerViewScrollListener(this.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                    if(!usersListViewModel.isInSearchMode && usersListAdapter.itemCount>10){
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
                            user.login!!.toLowerCase().contains(it!!.toString().toLowerCase()) || user.login.equals(
                                it.toString(), ignoreCase = true)
                        }

                    usersListAdapter.addUsers(filtered, true)
                }

            }
        }
    }
}