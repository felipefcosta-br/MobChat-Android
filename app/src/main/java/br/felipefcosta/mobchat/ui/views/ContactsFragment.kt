package br.felipefcosta.mobchat.ui.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentContactsBinding
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ContactsRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.AuthDataSource
import br.felipefcosta.mobchat.models.services.EncryptionManager
import br.felipefcosta.mobchat.models.services.ProfileDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import br.felipefcosta.mobchat.viewmodels.ContactsFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.ContactsViewModelFactory


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

        val repository = ContactsRepository()

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository = ProfileRepository(profileDataSource, tokenStorageManager)

        viewModel = ViewModelProvider(
            this,
            ContactsViewModelFactory(
                requireActivity().application,
                repository,
                profileRepository,
                authRepository
            )
        ).get(ContactsFragmentViewModel::class.java)



        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var arg = arguments?.getParcelable<Profile>("profileArg")
        if (arg != null){
            viewModel.profile = arg
        }

        Log.i("ProMIT", arg?.email.toString())

    }

}