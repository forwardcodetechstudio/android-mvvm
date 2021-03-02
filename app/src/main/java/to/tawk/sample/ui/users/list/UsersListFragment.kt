package to.tawk.sample.ui.users.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import to.tawk.sample.databinding.FragmentUsersListBinding
import to.tawk.sample.utils.Status

@AndroidEntryPoint
class UsersListFragment : Fragment() {

    private val usersListViewModel: UsersListViewModel by viewModels()
    private val usersListAdapter by lazy {
        UsersListAdapter(requireContext())
    }

    private lateinit var binding: FragmentUsersListBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(::binding.isInitialized.not()){
            binding = FragmentUsersListBinding.inflate(inflater,container,false)
        }
        binding.lifecycleOwner = this.viewLifecycleOwner
//        binding.viewModel= usersListViewModel
        setupUserList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserList()
    }

    private fun observeUserList() {
        usersListViewModel.users.observe(viewLifecycleOwner, {
            when(it.status){
                Status.LOADING->{

                }
                Status.SUCCESS ->{
                    usersListAdapter.submitList(it.data)
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
}