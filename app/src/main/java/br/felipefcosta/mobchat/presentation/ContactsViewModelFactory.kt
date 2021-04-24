package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ContactsRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.lang.IllegalArgumentException

class ContactsViewModelFactory(
    private val application: Application,
    private val contactsRepository: ContactsRepository,
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsFragmentViewModel::class.java)) {
            return ContactsFragmentViewModel(
                application,
                contactsRepository,
                profileRepository,
                authRepository,
            ) as T
        }
        throw IllegalArgumentException("This fragment is not compatible")
    }
}