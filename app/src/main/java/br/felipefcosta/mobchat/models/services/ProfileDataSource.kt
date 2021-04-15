package br.felipefcosta.mobchat.models.services

import android.util.Log
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.ProfileApiService
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataSource(private val profileApiService: ProfileApiService) {
    fun addUserProfile(
        header: String, profile: Profile,
        success: (Profile) -> Unit, failure: () -> Unit
    ) {
        runBlocking {
            val response = profileApiService.createProfile(header, profile)
            response.run {
                if (this.isSuccessful) {
                    if (response.isSuccessful) {
                        val profile = response.body() as Profile
                        success(profile)
                    } else {
                        failure()
                    }
                } else {
                    failure()
                }
            }
        }
    }

    fun getProfileByAccountId(
        header: String,
        accountId: String,
        success: (Profile) -> Unit,
        failure: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = profileApiService.getProfileByAccountId(header, accountId)
            response.run {
                if (this.isSuccessful) {
                    val profile = response.body() as Profile
                    success(profile)
                } else {
                    failure()
                }
            }
        }

    }

    fun searchProfile(
        header: String,
        searchTxt: String,
        success: (List<Profile>) -> Unit,
        failure: () -> Unit
    ) {
       runBlocking {
           var response = profileApiService.searchUserProfile(header, searchTxt)
           response.run {
               var profileList: List<Profile> = emptyList()
               if (this.isSuccessful){
                   profileList = response.body() as List<Profile>
                   success(profileList)
               }else{
                   failure()
               }
           }
       }
    }

    fun updateUserProfile(
        header: String, profile: Profile,
        success: (Boolean) -> Unit, failure: () -> Unit
    ) {

        runBlocking {

            val response = profileApiService.updateProfile(header, profile.id!!, profile)
            response.run {
                if (response.body() == true) {
                    success(response.body()!!)
                } else {
                    failure()
                }
            }
        }
    }
}