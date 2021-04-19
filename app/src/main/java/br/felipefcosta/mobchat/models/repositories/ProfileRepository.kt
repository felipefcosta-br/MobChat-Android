package br.felipefcosta.mobchat.models.repositories

import android.util.Log
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.services.ProfileDataSource
import br.felipefcosta.mobchat.models.services.ProfileStorageManager
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import kotlinx.coroutines.flow.Flow

class ProfileRepository(
    private val profileDataSource: ProfileDataSource,
    private val tokenStorageManager: TokenStorageManager,
    private val profileStorageManager: ProfileStorageManager
) {

    fun addProfile(
        profile: Profile,
        token: Token,
        success: (Profile) -> Unit,
        failure: () -> Unit
    ) {
        val header = "bearer ${token.accessToken.toString()}"
        profileDataSource.addUserProfile(header, profile, { profile ->
            success(profile)
        }, {
            Log.i("ProMIT", "repository")
            failure()
        })

    }

    fun getProfileByAccountId(accountId: String, success: (Profile) -> Unit, failure: () -> Unit){
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            profileDataSource.getProfileByAccountId(header, accountId, { profile ->
                success(profile)
            }, {
                Log.i("ProMIT", "repository")
                failure()
            })
        } else {
            failure()
        }
    }

    fun searchProfile(searchTxt: String, success: (List<Profile>) -> Unit, failure: () -> Unit){
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            profileDataSource.searchProfile(header, searchTxt, { profiles ->
                Log.i("ProMIT", profiles.toString())
                success(profiles)
            }, {
                Log.i("ProMIT", "repository")
                failure()
            })
        } else {
            failure()
        }
    }

    fun updateProfile(
        profile: Profile,
        success: (Boolean) -> Unit,
        failure: () -> Unit
    ) {
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            profileDataSource.updateUserProfile(header, profile, { response ->
                success(response)
            }, {
                Log.i("ProMIT", "repository")
                failure()

            })
        } else {
            failure()
        }

    }

    fun getProfileByEmail(email: String){

    }

    fun storeLocalProfile(profile: Profile){
        profileStorageManager.saveProfile(profile)
    }
    fun getStorageProfile():Profile?{
        return profileStorageManager.getProfile()
    }

}