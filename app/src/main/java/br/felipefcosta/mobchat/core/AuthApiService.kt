package br.felipefcosta.mobchat.core

import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AuthApiService {

    @POST("connect/token")
    @Headers("Content-type: application/x-www-form-urlencoded")
    suspend fun getToken(): Response<Token>

    @FormUrlEncoded
    @POST("connect/token")
    @Headers("Content-type: application/x-www-form-urlencoded")
    suspend fun getToken(@FieldMap authMap: Map<String, String>): Response<Token>

    companion object {

        fun create(username: String, password: String): AuthApiService {

            val authInterceptor = OAuthInterceptor(username, password)

            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(authInterceptor)
                .build()


            return Retrofit.Builder()
                .baseUrl(Constants.TOKEN_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create()
        }

        fun create(): AuthApiService {

            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.TOKEN_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create()
        }
    }
}