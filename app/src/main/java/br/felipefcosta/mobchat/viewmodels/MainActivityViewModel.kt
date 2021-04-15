package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatHubRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionState

class MainActivityViewModel(
    application: Application,
    private val chatHubRepository: ChatHubRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : AndroidViewModel(application), LifecycleObserver {
    lateinit var profile: Profile

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startConnection() {

        if (authRepository.isValidToken()) {
            var tokenTemp = authRepository.getStoragedToken()
            if (tokenTemp != null) {

                chatHubRepository.startConnection(tokenTemp)
                getUserProfile({
                    if (profile.id != null){
                        var isConnected = false
                        while (!isConnected){
                            if (chatHubRepository.isConnected() == HubConnectionState.CONNECTED){
                                chatHubRepository.connectToHub(profile.id!!)
                                isConnected = true
                            }
                        }
                    }

                }, {

                })
            }
        }

    }

    fun getUserProfile(success: (Profile) -> Unit, failure: () -> Unit) {
        val jwtToken = authRepository.decodeToken()
        if (jwtToken != null) {
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