package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatListRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository

class ChatListFragmentViewModel(
    application: Application,
    private val repositoryMain: ChatListRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository

) : AndroidViewModel(application) {
    lateinit var profile: Profile

    fun getUserProfile(success: (Profile) -> Unit, failure: () -> Unit) {
        val jwtToken = authRepository.decodeToken()
        if (jwtToken !== null) {
            profileRepository.getProfileByAccountId(jwtToken.jwtPayload.sub, {
                profile = it
                profileRepository.storeLocalProfile(profile)
                success(it)
            }, {
                failure()
            })
        } else {
            failure()
        }

    }
}