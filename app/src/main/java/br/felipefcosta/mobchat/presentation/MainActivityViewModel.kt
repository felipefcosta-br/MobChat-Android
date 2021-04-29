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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startConnection() {

        var token = authRepository.getStoragedToken()
        var profileTemp = profileRepository.getStorageProfile()

        /*if (token != null && profileTemp != null) {
            profile = profileTemp
            val userIdGuid = UUID.fromString(profile.id!!)

            //useInsecureSSL()
            SignalRHubService.startHubConnection(token, userIdGuid)
            runBlocking {
                SignalRHubService.connectToHub(profile.id!!)
            }

        }*/

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun finishConnection(){
            runBlocking {
                SignalRHubService.disconnect()
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

    /*fun useInsecureSSL() {

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        })

        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

        // Create all-trusting host name verifier
        val allHostsValid = HostnameVerifier { _, _ -> true }

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }*/


}