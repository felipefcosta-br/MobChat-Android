package br.felipefcosta.mobchat.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "access_token")
    var accessToken: String?,
    @Json(name = "token_type")
    val tokenType: String?,
    @Json(name = "expires_in")
    val expiresIn: String?,
    @Json(name = "scope")
    val scope: String?
) {
}