package br.felipefcosta.mobchat.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class JWTHeader(
    @Json(name = "alg")
    val alg: String,
    @Json(name = "kid")
    val kid: String,
    @Json(name = "typ")
    val typ: String
) {
}