package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AppUserRepository
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.services.AppUserDataSource
import java.lang.IllegalArgumentException

class FirstUserFormViewModelFactory(
    private val application: Application,
    private val repository: AppUserRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(FirstUserFormScreenFragmentViewModel::class.java))
            return FirstUserFormScreenFragmentViewModel(
                application,
                repository,
                authRepository
            ) as T

        throw IllegalArgumentException("This fragment is not compatible")

    }

}