package br.felipefcosta.mobchat.api

import br.felipefcosta.mobchat.utils.Constants
import okhttp3.*

class OAuthInterceptor(private val username: String, private val password: String) : Interceptor {

    private lateinit var credentials: String

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBody: RequestBody = FormBody.Builder()
            .add("grant_type", Constants.GRANT_TYPE)
            .add("username", username)
            .add("password", password)
            .add("client_id", Constants.CLIENT_ID).build()

        var request: Request = chain.request()
        var authRequest: Request = request.newBuilder()
            .post(requestBody)
            .build()

        return chain.proceed(authRequest)
    }
}