package br.felipefcosta.mobchat.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class JWTPayload(
    @Json(name = "nbf")
    val nbf: Int,
    @Json(name = "exp")
    val exp: Int,
    @Json(name = "iss")
    val iss: String,
    @Json(name = "aud")
    val aud: List<String>,
    @Json(name = "client_id")
    val client_id: String,
    @Json(name = "sub")
    val sub: String,
    @Json(name = "auth_time")
    val auth_time:Int,
    @Json(name = "idp")
    val idp: String,
    @Json(name = "role")
    val role: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "scope")
    val scope: List<String>,
    @Json(name = "amr")
    val amr: List<String>

) {
}
