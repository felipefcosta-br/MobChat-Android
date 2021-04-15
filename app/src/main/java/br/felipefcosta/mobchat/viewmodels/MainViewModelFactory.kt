package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatHubRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val application: Application,
    private val chatHubRepository: ChatHubRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(application, chatHubRepository, authRepository, profileRepository) as T
        }
        throw IllegalArgumentException("This fragment is not compatible")
    }
}