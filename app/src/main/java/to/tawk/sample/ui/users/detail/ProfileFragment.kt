package to.tawk.sample.ui.users.detail

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.engine.Resource
import dagger.hilt.android.AndroidEntryPoint
import to.tawk.sample.R
import to.tawk.sample.databinding.FragmentProfileBinding
import to.tawk.sample.utils.Status
import to.tawk.sample.utils.ViewExt.Companion.showSnackBar

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding:FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var arguments: ProfileFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments = ProfileFragmentArgs.fromBundle(requireArguments())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        binding.lifecycleOwner=this.viewLifecycleOwner
        binding.viewModel = profileViewModel
        profileViewModel.userName.value = arguments.username
        observeProfile()
        setupOnSave()
        setupBack()
    }

    private fun setupOnSave() {
            binding.btnSave.setOnClickListener {view->
                view.isEnabled=false
                profileViewModel.saveNote().observe(viewLifecycleOwner, {
                    view.isEnabled=true

                    when(it.status){
                        Status.LOADING ->{

                        }
                        Status.SUCCESS->{
                            view?.showSnackBar("Note saved")
                        }
                        Status.ERROR->{
                            view?.showSnackBar(it.message)
                        }
                    }
                })
            }
    }

    private fun observeProfile() {
        profileViewModel.profileLiveData.observe(viewLifecycleOwner, {result->

            when(result.status){
                Status.LOADING ->{

                }
                Status.SUCCESS->{
                    result.data?.let {
                        profileViewModel.profile.value= it
                        profileViewModel.notes.value = it.notes
                    }

                }
                Status.ERROR->{
                    view?.showSnackBar(result.message)
                }
            }
        })
    }

    private fun setupBack(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}