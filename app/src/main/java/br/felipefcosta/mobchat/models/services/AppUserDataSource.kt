package br.felipefcosta.mobchat.models.services

import android.util.Log
import br.felipefcosta.mobchat.models.dtos.UserPasswordDto
import br.felipefcosta.mobchat.models.entities.AppUser
import br.felipefcosta.mobchat.api.AppUserApiService
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.models.entities.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppUserDataSource(
    private val appUserApiService: AppUserApiService
) {

    fun addAppUser(
        header: String, userPasswordDto: UserPasswordDto,
        success: (AppUser) -> Unit, failure: () -> Unit
    ) {
        runBlocking {
            val response = appUserApiService.createUser(header, userPasswordDto)
            response.run {
                if (this.isSuccessful) {
                    if (response.body() != null){
                        val appUser = AppUser(
                            response.body()!!.id,
                            response.body()!!.username,
                            response.body()!!.email,
                            response.body()!!.emailConfirmed,
                            response.body()!!.phoneNumber,
                            response.body()!!.phoneNumberConfirmed,
                            response.body()!!.lockoutEnabled,
                            response.body()!!.twoFactorEnabled,
                            response.body()!!.accessFailedCount)
                        success(appUser)
                        Log.i("ProMIT", appUser.username.toString())
                    }
                } else {
                    failure()
                }
            }

        }

    }

    fun getAppUser(
        header: String,
        appUserId: String,
        success: (AppUser) -> Unit,
        failure: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = appUserApiService.getAppUser(header, appUserId)
            response.run {
                if (this.isSuccessful) {
                    val appUser = response.body() as AppUser
                    success(appUser)
                    Log.i("ProMIT", appUser.username.toString())
                } else {
                    failure()
                }
            }
        }
    }

}