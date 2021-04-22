package br.felipefcosta.mobchat.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.ChatApiService
import br.felipefcosta.mobchat.api.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentChatListBinding
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatListRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.viewmodels.ChatListFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.MainChatsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
List of all user chats
 */
class ChatListFragment : Fragment() {

    lateinit var binding: FragmentChatListBinding
    lateinit var viewModelMain: ChatListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat_list, container, false
        )

        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)
        val profileStorageManager = ProfileStorageManager(requireContext(), encryptionManager)

        /*val chatService = ChatApiService.create()
        val chatDataSource = ChatListDataSource() // adicionar o service*/
        val chatRepository = ChatListRepository()

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository =
            ProfileRepository(profileDataSource, tokenStorageManager, profileStorageManager)

        viewModelMain = ViewModelProvider(
            this,
            MainChatsViewModelFactory(
                requireActivity().application,
                chatRepository,
                authRepository,
                profileRepository
            )
        ).get(ChatListFragmentViewModel::class.java)

        startChatsFragment()

        viewModelMain.getUserProfile({ profile ->

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