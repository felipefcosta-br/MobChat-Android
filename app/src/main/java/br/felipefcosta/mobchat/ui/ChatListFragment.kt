package br.felipefcosta.mobchat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.core.AuthApiService
import br.felipefcosta.mobchat.core.ChatApiService
import br.felipefcosta.mobchat.core.ProfileApiService
import br.felipefcosta.mobchat.databinding.FragmentChatListBinding
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatListRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.ui.adapters.ChatListRecyclerViewAdapter
import br.felipefcosta.mobchat.ui.adapters.ChatListRecyclerViewItemListener
import br.felipefcosta.mobchat.presentation.ChatListFragmentViewModel
import br.felipefcosta.mobchat.presentation.ChatListViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
List of all user chats
 */
class ChatListFragment : Fragment(), ChatListRecyclerViewItemListener {

    lateinit var binding: FragmentChatListBinding
    lateinit var viewModel: ChatListFragmentViewModel

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

        val chatApiService = ChatApiService.create()
        val chatDataSource = ChatDataSource(chatApiService)
        val chatListRepository = ChatListRepository(chatDataSource, tokenStorageManager)

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository =
            ProfileRepository(profileDataSource, tokenStorageManager, profileStorageManager)

        viewModel = ViewModelProvider(
            this,
            ChatListViewModelFactory(
                requireActivity().application,
                chatListRepository,
                authRepository,
                profileRepository
            )
        ).get(ChatListFragmentViewModel::class.java)

        viewModel.chats.value = emptyList<Chat>()

        viewModel.getUserProfile({ profile ->

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

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ChatListRecyclerViewAdapter(viewModel.profile!!.id!!)
        adapter.setRecyclerViewItemListener(this)
        adapter.chatList = viewModel.chats.value!!

        binding.chatListRecyclerView.adapter = adapter
        viewModel.chats.observe(viewLifecycleOwner, {
            it.let {
                adapter.chatList = it
            }
        })

        DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL).apply {
            binding.chatListRecyclerView.addItemDecoration(this)
        }
    }

    override fun recyclerViewItemClicked(view: View, chat: Chat) {
        var profile = viewModel.profile

        if (profile == null || chat == null)
            return

        var contactName =
            if (profile.id == chat.firstMemberId) chat.secondMemberName else chat.firstMemberName

        val action = ChatListFragmentDirections.chatlistToChatAction(profile, chat, contactName!!)
        findNavController().navigate(action)
    }

}