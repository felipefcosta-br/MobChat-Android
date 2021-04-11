package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.repositories.StorageBlobRepository
import java.lang.IllegalArgumentException

class ThirdUserFormViewModelFactory(
    private val application: Application,
    private val repository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val storageBlobRepository: StorageBlobRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThirdUserFormScreenFragmentViewModel::class.java))
            return ThirdUserFormScreenFragmentViewModel(
                application,
                repository,
                authRepository,
                storageBlobRepository
            ) as T
        throw IllegalArgumentException("This fragment is not compatible")
    }
}