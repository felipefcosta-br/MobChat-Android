package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AppUserRepository
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.lang.IllegalArgumentException

class SplashViewModelFactory(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val appUserRepository: AppUserRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashFragmentViewModel::class.java)) {
            return SplashFragmentViewModel(
                application,
                authRepository,
                appUserRepository,
                profileRepository
            ) as T
        }
        throw IllegalArgumentException("This fragment is not compatible")
    }
}