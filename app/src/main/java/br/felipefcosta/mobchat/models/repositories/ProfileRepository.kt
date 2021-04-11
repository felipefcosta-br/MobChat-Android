package br.felipefcosta.mobchat.models.repositories

import android.util.Log
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.services.ProfileDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager

class ProfileRepository(
    private val remoteDataSource: ProfileDataSource,
    private val tokenStorageManager: TokenStorageManager
) {

    fun addProfile(
        profile: Profile,
        token: Token,
        success: (Profile) -> Unit,
        failure: () -> Unit
    ) {
        val header = "bearer ${token.accessToken.toString()}"
        remoteDataSource.addUserProfile(header, profile, { profile ->
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
            remoteDataSource.getProfileByAccountId(header, accountId, { profile ->
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
            remoteDataSource.searchProfile(header, searchTxt, { profile ->
                success(profile)
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
        success: (Profile) -> Unit,
        failure: () -> Unit
    ) {
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            remoteDataSource.addUserProfile(header, profile, { profile ->
                success(profile)
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

}