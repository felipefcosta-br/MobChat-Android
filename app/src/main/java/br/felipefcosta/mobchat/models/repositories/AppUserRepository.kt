package br.felipefcosta.mobchat.models.repositories

import android.util.Log
import br.felipefcosta.mobchat.models.dtos.UserPasswordDto
import br.felipefcosta.mobchat.models.entities.AppUser
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.services.AppUserDataSource

class AppUserRepository(private val remoteDataSource: AppUserDataSource) {

    fun addAppUser(
        token: Token,
        userPasswordDto: UserPasswordDto,
        success: (AppUser) -> Unit,
        failure: () -> Unit
    ) {
        if (!token.accessToken.isNullOrBlank()) {
            val header = "bearer ${token.accessToken.toString()}"
            Log.i("ProMIT", "repository - ${token.accessToken.toString()}")
            remoteDataSource.addAppUser(header, userPasswordDto, { newUser ->
                Log.i("ProMIT", "repository - ${newUser.email}")
                success(newUser)
            }, {
                failure()
                Log.i("ProMIT", "repository")
            })
        }
    }

    fun getAppUser(
        token: Token,
        appUserId: String,
        success: (AppUser) -> Unit,
        failure: () -> Unit
    ) {
        if (!token.accessToken.isNullOrBlank()) {
            val header = "bearer ${token.accessToken.toString()}"
            remoteDataSource.getAppUser(header, appUserId, { appUser ->
                success(appUser)
            }, {
                failure()
            })
        }
    }

}