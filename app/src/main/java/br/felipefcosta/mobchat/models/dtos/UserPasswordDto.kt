package br.felipefcosta.mobchat.models.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserPasswordDto(
    @Json(name = "user")
    val user: User,
    @Json(name = "password")
    val password: Password

) {
}
@JsonClass(generateAdapter = true)
class User(
    @Json(name = "userName")
    val userName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "emailConfirmed")
    val emailConfirmed: Boolean,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "phoneNumberConfirmed")
    val phoneNumberConfirmed: Boolean,
    @Json(name = "lockoutEnabled")
    val lockoutEnabled: Boolean
) {

}
@JsonClass(generateAdapter = true)
class Password(
    @Json(name = "password")
    val password: String,
    @Json(name = "confirmPassword")
    val confirmPassword: String
) {

}



