package br.felipefcosta.mobchat.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.ChatApiService
import br.felipefcosta.mobchat.databinding.FragmentChatBinding
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.repositories.ChatRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.ui.adapters.MessagesRecyclerViewAdapter
import br.felipefcosta.mobchat.ui.adapters.MessagesRecyclerViewItemListener
import br.felipefcosta.mobchat.viewmodels.ChatFragmentViewModel
import br.felipefcosta.mobchat.viewmodels.ChatViewModelFactory


class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var viewModel: ChatFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat, container, false
        )

        val encryptionManager = EncryptionManager(requireContext())
        val tokenStorageManager = TokenStorageManager(requireContext(), encryptionManager)
        val chatApiService = ChatApiService.create()

        val chatDataSource = ChatDataSource(chatApiService)
        val repository = ChatRepository(chatDataSource, tokenStorageManager)


        viewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(requireActivity().application, repository)
        ).get(ChatFragmentViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        val bundle = arguments
        if (bundle != null) {
            val args = ChatFragmentArgs.fromBundle(bundle)
            if (args.profileArg != null || args.contactProfileArg != null) {
                viewModel.profile = args.profileArg
                viewModel.contactProfile = args.contactProfileArg
            }
        }
        viewModel.initChat()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle == null) {
            Log.e("Confirmation", "ConfirmationFragment did not receive action information")
            return
        }

        val args = ChatFragmentArgs.fromBundle(bundle)
        val profileArg = args.profileArg as Profile
        val contactsArg = args.contactProfileArg as Profile

        if (profileArg.id == null && contactsArg.id == null) {
            return
        }

        var linearLayoutManager = LinearLayoutManager(requireActivity().applicationContext)
        linearLayoutManager.stackFromEnd = true;
        linearLayoutManager.reverseLayout = false;
        binding.chatMessagesRecyclerView.layoutManager = linearLayoutManager

        viewModel.messages.value = emptyList<TextMessage>()

        val adapter = MessagesRecyclerViewAdapter(profileArg.id!!)
        adapter.messagesList = viewModel.messages.value!!

        binding.chatMessagesRecyclerView.adapter = adapter
        viewModel.messages.observe(viewLifecycleOwner, {
            it.let {
                adapter.messagesList = it
            }
        })

    }
}