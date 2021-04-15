package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.models.entities.AppUser
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.repositories.AppUserRepository
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.utils.Constants

class SplashFragmentViewModel(
    application: Application,
    private val authRepository: AuthRepository,
    private val appUserRepository: AppUserRepository,
    private val profileRepository: ProfileRepository
) : AndroidViewModel(application) {

    fun getUserProfile(success: (Profile) -> Unit, failure: () -> Unit) {
        val jwtToken = authRepository.decodeToken()
        if (jwtToken != null) {
            profileRepository.getProfileByAccountId(jwtToken.jwtPayload.sub, {
                success(it)
            }, {
                failure()
            })
        } else {
            failure()
        }

    }

    fun getAppUser(success: (AppUser) -> Unit, failure: () -> Unit) {
        var authMap = HashMap<String, String>()
        authMap["grant_type"] = Constants.GRANT_TYPE
        authMap["userName"] = Constants.EMAIL
        authMap["password"] = Constants.PASSWORD
        authMap["client_id"] = Constants.CLIENT_ID

        val jwtToken = authRepository.decodeToken()
        if (jwtToken !== null) {
            authRepository.getAdminToken(authMap, { token ->
                if (!token.accessToken.isNullOrBlank()) {
                    appUserRepository.getAppUser(token, jwtToken.jwtPayload.sub, { appUser ->
                        success(appUser)
                    }, {
                        failure()
                    })
                } else {
                    failure()
                }
            }, {
                failure()
            })

        } else {
            failure()
        }
    }

    fun getToken(success: (Token) -> Unit, failure: () -> Unit) {
        authRepository.getToken({
            success(it)
        }, {
            failure()
        })
    }

    fun isValideToken(): Boolean {
        return authRepository.isValidToken()
    }
}