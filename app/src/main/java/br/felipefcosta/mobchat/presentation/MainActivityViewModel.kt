package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.core.SignalRHubService
import kotlinx.coroutines.runBlocking
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class MainActivityViewModel(
    application: Application,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : AndroidViewModel(application), LifecycleObserver {
    lateinit var profile: Profile

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initUserProfile(){
        var profileTemp = profileRepository.getStorageProfile()
        if (profileTemp == null){
            getUserProfile({
                profile = it
            }, {
                //return@getUserProfile
            })
        }else{
            profile = profileTemp
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun finishConnection(){
        runBlocking {
            SignalRHubService.disconnect()
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