package br.felipefcosta.mobchat.models.services

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Base64
import android.util.Log
import br.felipefcosta.mobchat.models.dtos.JWTTokenDto
import br.felipefcosta.mobchat.models.entities.JWTHeader
import br.felipefcosta.mobchat.models.entities.JWTPayload
import br.felipefcosta.mobchat.models.entities.Token
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class TokenStorageManager(val context: Context, val encryptionManager: EncryptionManager) {

    private var token: Token? = null
    private val applicationName = "br.felipefcosta.mobchat"
    private val key = "AUTH_STATE"
    private val sharedPref = context.getSharedPreferences(applicationName, MODE_PRIVATE)

    fun getToken(): Token? {

        if (token != null)
            return token

        loadToken()
        return token
    }

    fun saveToken(newToken: Token) {
        token = newToken
        storeToken()
    }

    fun removeToken() {
        token = null
        sharedPref.edit().remove(key).apply()
    }

    fun expireAccessToken() {
        if (token != null) {
            token!!.accessToken = "x${token!!.accessToken}"
        }
    }

    fun expireRefreshToken() {
        if (token != null) {
            token!!.accessToken = null
            storeToken()
        }
    }

    private fun loadToken() {

        if (token != null)
            return

        val encryptedData = sharedPref.getString(key, "")
        if (encryptedData.isNullOrBlank()) {
            return
        }

        var decryptedData: String?
        try {

            decryptedData = encryptionManager.decrypt(encryptedData)

            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Token> = moshi.adapter(Token::class.java)
            token = jsonAdapter.fromJson(decryptedData)


        } catch (ex: Exception) {
            throw IllegalArgumentException()

        }
    }

    private fun storeToken() {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Token> = moshi.adapter(Token::class.java)
        val tokenJson = jsonAdapter.toJson(token)
        val encryptedData = encryptionManager.encrypt(tokenJson)
        sharedPref.edit().putString(key, encryptedData).apply()
    }

    fun tokenDecoder(): JWTTokenDto? {
        loadToken()
        if (token?.accessToken == null)
            return null

        val tokenStr = token?.accessToken.toString()
        val parts = tokenStr.split(".")

        var header = Base64.decode(parts[0], Base64.URL_SAFE).toString(Charsets.UTF_8)
        var payload = Base64.decode(parts[1], Base64.URL_SAFE).toString(Charsets.UTF_8)

        val moshi = Moshi.Builder().build()
        val jsonHeaderAdapter: JsonAdapter<JWTHeader> =
            moshi.adapter<JWTHeader>(JWTHeader::class.java)
        val jwtHeader = jsonHeaderAdapter.fromJson(header)

        val jsonPayloadAdapter: JsonAdapter<JWTPayload> =
            moshi.adapter<JWTPayload>(JWTPayload::class.java)
        val jwtPayload = jsonPayloadAdapter.fromJson(payload)

        if (jwtHeader == null && jwtPayload == null)
            return null

        return JWTTokenDto(jwtHeader!!, jwtPayload!!)
    }

    fun isValidToken(): Boolean {
        var jwtTokenDto = tokenDecoder()
        loadToken()
        val tokenStr = token?.expiresIn.toString()
        Log.i("ProMIT", tokenStr.toString())

        if (jwtTokenDto?.jwtPayload?.exp != null) {

            val expDate = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(jwtTokenDto?.jwtPayload?.exp.toLong()),
                ZoneId.systemDefault()
            )
            val currentDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneId.systemDefault())

            if (currentDate.isBefore(expDate))
                return true
        }
        return false
    }
}