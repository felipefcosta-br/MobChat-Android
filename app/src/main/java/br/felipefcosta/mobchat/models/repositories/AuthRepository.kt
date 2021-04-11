package br.felipefcosta.mobchat.models.repositories

import android.util.Log
import br.felipefcosta.mobchat.models.dtos.JWTTokenDto
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.services.AuthDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager

class AuthRepository(
    private val remoteDataSource: AuthDataSource,
    private val tokenStorageManager: TokenStorageManager
) {

    fun getToken(success: (Token) -> Unit, failure: () -> Unit) {
        remoteDataSource.getToken({token ->
            if (!token.accessToken.isNullOrBlank()){
                tokenStorageManager.saveToken(token)
                Log.i("ProMIT", "repository - ${token.accessToken.toString()}")
                success(token)
            }else{
                failure()
            }

        }, {
            failure()
        })
    }

    fun getToken(authMap: Map<String, String>, success: (Token) -> Unit, failure: () -> Unit) {
        remoteDataSource.getToken(authMap, {token ->
            if (!token.accessToken.isNullOrBlank()){
                tokenStorageManager.saveToken(token)
                Log.i("ProMIT", "repository - ${token.accessToken.toString()}")
                success(token)
            }else{
                failure()
            }

        }, {
            failure()
        })
    }

    fun getAdminToken(success: (Token) -> Unit, failure: () -> Unit) {
        remoteDataSource.getToken({
            success(it)
        }, {
            failure()
        })
    }

    fun getAdminToken(authMap: Map<String, String>, success: (Token) -> Unit, failure: () -> Unit) {
        remoteDataSource.getToken(authMap, {token ->
            success(token)
        }, {
            failure()
        })
    }

    fun isValidToken(): Boolean{
        return tokenStorageManager.isValidToken()
    }

    fun decodeToken(): JWTTokenDto? {
        return  tokenStorageManager.tokenDecoder()
    }
}