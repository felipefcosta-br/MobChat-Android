package br.felipefcosta.mobchat.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgument
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.ChatsApiService
import br.felipefcosta.mobchat.api.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentChatsBinding
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatsRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.viewmodels.ChatsFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.ChatsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
List of all user chats
 */
class ChatsFragment : Fragment() {

    lateinit var binding: FragmentChatsBinding
    lateinit var viewModel: ChatsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chats, container, false
        )

        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)

        val chatsService = ChatsApiService.create()
        val chatsDataSource = ChatsDataSource() // adicionar o service
        val chatsRepository = ChatsRepository()

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository = ProfileRepository(profileDataSource, tokenStorageManager)

        viewModel = ViewModelProvider(
            this,
            ChatsViewModelFactory(
                requireActivity().application,
                chatsRepository,
                authRepository,
                profileRepository
            )
        ).get(ChatsFragmentViewModel::class.java)

        startChatsFragment()

        viewModel.getUserProfile({profile ->

            val navController = findNavController()
            val arg = Bundle()
            arg.putParcelable("profileArg", profile)
            //val bundle = bundleOf("profileArg" to profile)

            val mainNav = activity?.findViewById<BottomNavigationView>(R.id.main_nav_view)
            mainNav?.setupWithNavController(navController)
            mainNav?.setOnNavigationItemSelectedListener {
                navController.navigate(it.itemId, arg)
                true
            }

        }, {

        })



        return binding.root
    }

    private fun startChatsFragment() {

    }

}