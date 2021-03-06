package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.lang.IllegalArgumentException

class SecondUserFormViewModelFactory(
    private val application: Application,
    private val repository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondUserFormScreenFragmentViewModel::class.java))
            return SecondUserFormScreenFragmentViewModel(application, repository, authRepository) as T

        throw IllegalArgumentException("This fragment is not compatible")
    }
}