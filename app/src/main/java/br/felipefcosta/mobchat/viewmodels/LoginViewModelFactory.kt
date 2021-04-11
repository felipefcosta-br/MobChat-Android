package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import java.lang.IllegalArgumentException

class LoginViewModelFactory(
    private val application: Application,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginFragmentViewModel::class.java))
            return LoginFragmentViewModel(application, authRepository) as T

        throw IllegalArgumentException("This fragment is not compatible")

    }

}