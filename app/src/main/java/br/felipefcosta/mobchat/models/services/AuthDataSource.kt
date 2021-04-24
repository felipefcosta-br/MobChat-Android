package br.felipefcosta.mobchat.models.services

import android.util.Log
import br.felipefcosta.mobchat.core.AuthApiService
import br.felipefcosta.mobchat.models.entities.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthDataSource(private val authApiService: AuthApiService) {

    fun getToken(success: (Token) -> Unit, failure: () -> Unit) {
        val call = authApiService?.getToken()
        call?.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    val token = Token(
                        response.body()?.accessToken,
                        response.body()?.tokenType,
                        response.body()?.expiresIn,
                        response.body()?.scope
                    )
                    Log.i("ProMIT", token.accessToken.toString())
                    success(token)

                } else {
                    Log.i("ProMIT", "datasource")
                    failure()
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                failure()
            }

        })
    }

    fun getToken(authMap: Map<String, String>, success: (Token) -> Unit, failure: () -> Unit) {

        val call = authApiService?.getToken(authMap)
        call?.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    val token = Token(
                        response.body()?.accessToken,
                        response.body()?.tokenType,
                        response.body()?.expiresIn,
                        response.body()?.scope
                    )
                    Log.i("ProMIT", token.accessToken.toString())
                    success(token)

                } else {
                    Log.i("ProMIT", "datasource")
                    failure()
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                failure()
            }

        })
    }
}