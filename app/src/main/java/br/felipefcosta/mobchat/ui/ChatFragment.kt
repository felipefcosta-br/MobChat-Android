
package br.felipefcosta.mobchat.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.core.ChatApiService
import br.felipefcosta.mobchat.core.TextMessageApiService
import br.felipefcosta.mobchat.databinding.FragmentChatBinding
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.repositories.ChatRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.ui.adapters.MessagesRecyclerViewAdapter
import br.felipefcosta.mobchat.presentation.ChatFragmentViewModel
import br.felipefcosta.mobchat.presentation.ChatViewModelFactory


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
        val chatStorageManager = ChatStorageManager(requireContext(), encryptionManager)

        val chatApiService = ChatApiService.create()
        val chatDataSource = ChatDataSource(chatApiService)

        val textMessageApiService = TextMessageApiService.create()
        val textMessageDataSource = TextMessageDataSource(textMessageApiService)
        val repository = ChatRepository(
            chatDataSource,
            textMessageDataSource,
            tokenStorageManager,
            chatStorageManager
        )

        viewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(requireActivity().application, repository)
        ).get(ChatFragmentViewModel::class.java)


        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        lifecycle.addObserver(viewModel)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            parentFragmentManager.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle == null) {
            Log.e("Confirmation", "ConfirmationFragment did not receive action information")
            return
        }
        val args = ChatFragmentArgs.fromBundle(bundle)

        if (args.profileArg != null) {

            viewModel.profile = args.profileArg

            if (args.chatArg != null) {
                viewModel.chat = args.chatArg
                viewModel.chatId = args.chatArg.id
                if (args.chatArg.firstMemberId == args.profileArg.id) {
                    viewModel.contactId = args.chatArg.secondMemberId
                    viewModel.contactName = args.chatArg.secondMemberName
                    viewModel.contactPhoto = args.chatArg.secondMemberPhoto

                } else if (args.chatArg.secondMemberId == args.profileArg.id) {
                    viewModel.contactId = args.chatArg.firstMemberId
                    viewModel.contactName = args.chatArg.firstMemberName
                    viewModel.contactPhoto = args.chatArg.firstMemberPhoto

                }

            } else if (args.contactProfileArg != null) {
                viewModel.contactProfile = args.contactProfileArg
                viewModel.contactId = args.contactProfileArg.id
                viewModel.contactName =
                    "${args.contactProfileArg.name} ${args.contactProfileArg.surname}"
                viewModel.contactPhoto = args.contactProfileArg.photo
            }

        }


        viewModel.initChat()

        if (viewModel.profile.id == null) {
            return
        }

        var linearLayoutManager = LinearLayoutManager(requireActivity().applicationContext)
        linearLayoutManager.stackFromEnd = true;
        linearLayoutManager.reverseLayout = false;
        binding.chatMessagesRecyclerView.layoutManager = linearLayoutManager


        viewModel.messages.value = emptyList<TextMessage>()

        val adapter = MessagesRecyclerViewAdapter(viewModel.profile.id!!)
        adapter.messagesList = viewModel.messages.value!!

        binding.chatMessagesRecyclerView.adapter = adapter
        viewModel.messages.observe(viewLifecycleOwner, {
            it.let {
                adapter.messagesList = it
            }
        })
        viewModel.messagesRecyclerView = binding.chatMessagesRecyclerView

    }

}