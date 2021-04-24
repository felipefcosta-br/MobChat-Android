package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatListRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.lang.IllegalArgumentException

class ChatListViewModelFactory(
    private val application: Application,
    private val repository: ChatListRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListFragmentViewModel::class.java)) {
            return ChatListFragmentViewModel(application, repository, authRepository, profileRepository) as T
        }
        throw IllegalArgumentException("This fragment is not compatible")
    }
}