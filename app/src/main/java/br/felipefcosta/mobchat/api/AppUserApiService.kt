package br.felipefcosta.mobchat.api

import android.util.Log
import br.felipefcosta.mobchat.models.dtos.UserPasswordDto
import br.felipefcosta.mobchat.models.entities.AppUser
import br.felipefcosta.mobchat.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AppUserApiService {

    @POST("Users/AppUsers")
    suspend fun createUser(
        @Header("Authorization") header: String,
        @Body userPassword: UserPasswordDto
    ): Response<AppUser>

    @GET("Users/{id}")
    suspend fun getAppUser(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<AppUser>

    companion object{
        fun create(): AppUserApiService {

            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("ProMIT", message)
                }
            })
            logging.level = (HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
            
            return Retrofit.Builder()
                .baseUrl(Constants.APPUSER_API_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create()
        }
    }
}