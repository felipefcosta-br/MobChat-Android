package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatsRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.lang.IllegalArgumentException

class ChatsViewModelFactory(
    private val application: Application,
    private val chatsRepository: ChatsRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatsFragmentViewModel::class.java)) {
            return ChatsFragmentViewModel(application, chatsRepository, authRepository, profileRepository) as T
        }
        throw IllegalArgumentException("This fragment is not compatible")
    }
}