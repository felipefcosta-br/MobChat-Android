package br.felipefcosta.mobchat.api

import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ChatApiService {

    @GET("Chats/{id}")
    suspend fun getChat(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<Chat>

    @GET("Chats/member/{memberId}")
    suspend fun getChatsByUserId(
        @Header("Authorization") header: String,
        @Path("memberId") memberId: String
    ): Response<List<Chat>>

    @GET("Chats/members/{firstMemberId}/{secondMemberId}")
    suspend fun getChatByUserIdAndContactId(
        @Header("Authorization") header: String,
        @Path("firstMemberId") firstMemberId: String,
        @Path("secondMemberId") secondMemberId: String
    ): Response<Chat>


    companion object {
        fun create(): ChatApiService {

            /*val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("ProMIT", message)
                }
            })
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            */

            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.CHATHUB_API_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create()
        }
    }
}