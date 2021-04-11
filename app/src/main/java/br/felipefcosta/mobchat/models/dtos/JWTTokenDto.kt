package br.felipefcosta.mobchat.models.dtos

import br.felipefcosta.mobchat.models.entities.JWTHeader
import br.felipefcosta.mobchat.models.entities.JWTPayload
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class JWTTokenDto(
    @Json(name = "jwt_header")
    val jwtHeader: JWTHeader,
    @Json(name = "jwt_payload")
    val jwtPayload: JWTPayload
) {
}