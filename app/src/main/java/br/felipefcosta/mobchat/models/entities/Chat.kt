package br.felipefcosta.mobchat.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Chat(
    @Json(name = "firstMemberId")
    var firstMemberId: String,
    @Json(name = "firstMemberName")
    var firstMemberName: String,
    @Json(name = "firstMemberPhoto")
    var firstMemberPhoto: String,
    @Json(name = "secondMemberId")
    var secondMemberId: String,
    @Json(name = "secondMemberName")
    var secondMemberName: String,
    @Json(name = "secondMemberPhoto")
    var secondMemberPhoto: String,
    @Json(name = "firstMessageDate")
    var firstMessageDate: String,
    @Json(name = "lastMessageDate")
    var lastMessageDate: String,
    @Json(name = "status")
    var status: String,
    @Json(name = "isOnline")
    var isOnline: Boolean,
    @Json(name = "id")
    val id: String?
) {
    @JvmOverloads
    constructor(
        @Json(name = "firstMemberId")
        firstMemberId: String,
        @Json(name = "firstMemberName")
        firstMemberName: String,
        @Json(name = "firstMemberPhoto")
        firstMemberPhoto: String,
        @Json(name = "secondMemberId")
        secondMemberId: String,
        @Json(name = "secondMemberName")
        secondMemberName: String,
        @Json(name = "secondMemberPhoto")
        secondMemberPhoto: String,
        @Json(name = "firstMessageDate")
        firstMessageDate: String,
        @Json(name = "lastMessageDate")
        lastMessageDate: String,
        @Json(name = "status")
        status: String,
        @Json(name = "isOnline")
        isOnline: Boolean
    ) : this(
        firstMemberId,
        firstMemberName,
        firstMemberPhoto,
        secondMemberId,
        secondMemberName,
        secondMemberPhoto,
        firstMessageDate,
        lastMessageDate,
        status,
        isOnline,
        id = null
    )
}
