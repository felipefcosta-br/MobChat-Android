package br.felipefcosta.mobchat.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.SignalRApiService
import br.felipefcosta.mobchat.databinding.FragmentChatBinding
import br.felipefcosta.mobchat.models.repositories.ChatHubRepository
import br.felipefcosta.mobchat.models.repositories.ChatRepository
import br.felipefcosta.mobchat.models.services.ChatDataSource
import br.felipefcosta.mobchat.models.services.ChatHubDataSource
import br.felipefcosta.mobchat.models.services.ProfileStorageManager
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
        val chatDataSource = ChatDataSource()
        val repository = ChatRepository(chatDataSource)

        val signalRApiService = SignalRApiService()
        val chatHubDataSource = ChatHubDataSource(signalRApiService)
        val chatHubRepository = ChatHubRepository(chatHubDataSource)

        viewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(requireActivity().application, repository, chatHubRepository)
        ).get(ChatFragmentViewModel::class.java)

        val bundle = arguments
        if (bundle != null) {
            val args = ChatFragmentArgs.fromBundle(bundle)
            if (args.profileArg != null || args.contactProfileArg != null){
                viewModel.profile = args.profileArg
                viewModel.contactProfile = args.contactProfileArg
            }
        }
        return binding.root
    }
}