package br.felipefcosta.mobchat.api

import android.util.Log
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AuthApiService {

    @POST("connect/token")
    @Headers("Content-type: application/x-www-form-urlencoded")
    fun getToken(): Call<Token>

    @FormUrlEncoded
    @POST("connect/token")
    @Headers("Content-type: application/x-www-form-urlencoded")
    fun getToken(@FieldMap authMap: Map<String, String>): Call<Token>

    companion object {

        fun create(username: String, password: String): AuthApiService {

            val authInterceptor = OAuthInterceptor(username, password)

            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
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

            /*val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("ProMIT", message)
                }
            })
            logging.level = (HttpLoggingInterceptor.Level.BODY)*/

            val client = OkHttpClient.Builder()
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