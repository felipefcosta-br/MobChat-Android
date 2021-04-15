package br.felipefcosta.mobchat.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Chat(
    @Json(name = "FirstMemberId")
    var firstMemberId: String,
    @Json(name = "FirstMemberName")
    var firstMemberName: String,
    @Json(name = "FirstMemberPhoto")
    var firstMemberPhoto: String,
    @Json(name = "SecondMemberId")
    var secondMemberId: String,
    @Json(name = "SecondMemberName")
    var secondMemberName: String,
    @Json(name = "SecondMemberPhoto")
    var secondMemberPhoto: String,
    @Json(name = "FirstMessageDate")
    var firstMessageDate: String,
    @Json(name = "LastMessageDate")
    var lastMessageDate: String,
    @Json(name = "Status")
    var status: String,
    @Json(name = "IsOnline")
    var isOnline: Boolean
) {
    var id: String = ""
    @JvmOverloads
    constructor(
        @Json(name = "Id")
        id: String,
        @Json(name = "FirstMemberId")
        firstMemberId: String,
        @Json(name = "FirstMemberName")
        firstMemberName: String,
        @Json(name = "FirstMemberPhoto")
        firstMemberPhoto: String,
        @Json(name = "SecondMemberId")
        secondMemberId: String,
        @Json(name = "SecondMemberName")
        secondMemberName: String,
        @Json(name = "SecondMemberPhoto")
        secondMemberPhoto: String,
        @Json(name = "FirstMessageDate")
        firstMessageDate: String,
        @Json(name = "LastMessageDate")
        lastMessageDate: String,
        @Json(name = "Status")
        status: String,
        @Json(name = "IsOnline")
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
        isOnline
    ){
        this.id = id
    }
}
