package br.felipefcosta.mobchat.core

import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ProfileApiService {

    @POST("Profiles")
    suspend fun createProfile(
        @Header("Authorization") header: String,
        @Body profile: Profile
    ): Response<Profile>

    @GET("Profiles/{id}")
    suspend fun getProfile(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<Profile>

    @GET("Profiles/account/{accountId}")
    suspend fun getProfileByAccountId(
        @Header("Authorization") header: String,
        @Path("accountId") accountId: String
    ): Response<Profile>

    @PUT("Profiles/{id}")
    suspend fun updateProfile(
        @Header("Authorization") header: String,
        @Path(value = "id") id: String,
        @Body profile: Profile
    ): Response<Boolean>

    @GET("Profiles/search/{searchTxt}")
    suspend fun searchUserProfile(
        @Header("Authorization") header: String,
        @Path("searchTxt") searchTxt: String
    ): Response<List<Profile>>

    @GET("Profiles")
    fun getAllProfiles(@Header("Authorization") header: String): Call<List<Profile>>

    companion object {
        fun create(): ProfileApiService {

            /*val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("ProMIT", message)
                }
            })
            logging.level = (HttpLoggingInterceptor.Level.BODY)*/


            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.PROFILE_API_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create()
        }
    }
}