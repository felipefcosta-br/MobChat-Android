package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ContactsRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository

class ContactsFragmentViewModel(
    application: Application,
    private val repository: ContactsRepository,
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {

    lateinit var profile: Profile
}