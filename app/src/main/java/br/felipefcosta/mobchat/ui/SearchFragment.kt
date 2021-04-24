package br.felipefcosta.mobchat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.core.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentSearchBinding
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.EncryptionManager
import br.felipefcosta.mobchat.models.services.ProfileDataSource
import br.felipefcosta.mobchat.models.services.ProfileStorageManager
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import br.felipefcosta.mobchat.ui.adapters.ProfileRecyclerViewAdapter
import br.felipefcosta.mobchat.ui.adapters.SearchProfileRecyclerViewItemListener
import br.felipefcosta.mobchat.ui.SearchFragmentDirections
import br.felipefcosta.mobchat.presentation.SearchFragmentViewModel
import br.felipefcosta.mobchat.presentation.SearchViewModelFactory

class SearchFragment : Fragment(), SearchProfileRecyclerViewItemListener {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: SearchFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search, container, false
        )
        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)
        val profileStorageManager = ProfileStorageManager(requireContext(), encryptionManager)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository =
            ProfileRepository(profileDataSource, tokenStorageManager, profileStorageManager)

        viewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(requireActivity().application, profileRepository)
        ).get(SearchFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.profiles.value = emptyList<Profile>()

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ProfileRecyclerViewAdapter()
        adapter.setRecyclerViewItemListener(this)
        adapter.profileList = viewModel.profiles.value!!

        binding.searchRecyclerView.adapter = adapter
        viewModel.profiles.observe(viewLifecycleOwner, {
            it.let {
                adapter.profileList = it
            }
        })


        binding.searchProfileTextField.doAfterTextChanged {
            viewModel.searchContacts(binding.searchProfileTextField.text.toString())
            if (binding.searchProfileTextField.text.isNullOrBlank()){
                viewModel.profiles.value = emptyList<Profile>()
           }

        }
        /*binding.searchProfileButton.setOnClickListener {

            if (viewModel.profiles.value != null){
                adapter.profileList = viewModel.profiles.value!!
            }else{
                Log.i("ProMIT", "nulo")
            }
            viewModel.searchContacts(binding.searchProfileTextField.text.toString())
        }*/

        return binding.root
    }

    override fun recyclerViewItemClicked(view: View, contactprofile: Profile) {
        var profile = viewModel.getProfile()
        if (profile == null || contactprofile == null)
            return
        val contactName = "${contactprofile.name} ${contactprofile.surname}"
        val action =
            SearchFragmentDirections.searchToChatAction(profile, contactprofile, contactName)
        findNavController().navigate(action)
    }
}