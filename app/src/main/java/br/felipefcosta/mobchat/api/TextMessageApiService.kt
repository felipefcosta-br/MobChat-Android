package br.felipefcosta.mobchat.api

import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.TextMessage
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

interface TextMessageApiService {

    @GET("TextMessages/SenderAndReceiver/{senderId}/{receiverId}")
    suspend fun getTextMessageByUserIdAndContactId(
        @Header("Authorization") header: String,
        @Path("senderId") senderId: String,
        @Path("receiverId") receiverId: String
    ): Response<List<TextMessage>>

    @GET("TextMessages/Chat/{chatId}")
    suspend fun getTextMessagesByChatId(
        @Header("Authorization") header: String,
        @Path("chatId") chatId: String
    ): Response<List<TextMessage>>

    companion object {
        fun create(): TextMessageApiService {

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
                .baseUrl(Constants.CHAT_API_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create()
        }
    }
}