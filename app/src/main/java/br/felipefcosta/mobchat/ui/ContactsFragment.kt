package br.felipefcosta.mobchat.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.core.AuthApiService
import br.felipefcosta.mobchat.core.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentContactsBinding
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ContactsRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.presentation.ContactsFragmentViewModel
import br.felipefcosta.mobchat.presentation.ContactsViewModelFactory


/**

 */
class ContactsFragment : Fragment() {

    lateinit var binding: FragmentContactsBinding
    lateinit var viewModel: ContactsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_contacts, container, false
        )

        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)
        val profileStorageManager = ProfileStorageManager(requireContext(), encryptionManager)

        val repository = ContactsRepository()

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository =
            ProfileRepository(profileDataSource, tokenStorageManager, profileStorageManager)

        viewModel = ViewModelProvider(
            this,
            ContactsViewModelFactory(
                requireActivity().application,
                repository,
                profileRepository,
                authRepository
            )
        ).get(ContactsFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var arg = arguments?.getParcelable<Profile>("profileArg")
        if (arg != null) {
            viewModel.profile = arg
        }

        Log.i("ProMIT", arg?.email.toString())

    }

}