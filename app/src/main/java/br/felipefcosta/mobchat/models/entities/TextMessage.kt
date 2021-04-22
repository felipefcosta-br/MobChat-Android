package br.felipefcosta.mobchat.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextMessage(
    @Json(name = "chatId")
    var chatId: String?,
    @Json(name = "senderId")
    var senderId: String,
    @Json(name = "senderName")
    var senderName: String,
    @Json(name = "senderPhoto")
    var senderPhoto: String,
    @Json(name = "receiverId")
    var receiverId: String,
    @Json(name = "receiverName")
    var receiverName: String,
    @Json(name = "receiverPhoto")
    var receiverPhoto: String,
    @Json(name = "messageDate")
    var messageDate: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "status")
    var status: String,
    @Json(name = "id")
    val id: String?
) {

    @JvmOverloads
    constructor(
        @Json(name = "chatId")
        chatId: String?,
        @Json(name = "senderId")
        senderId: String,
        @Json(name = "senderName")
        senderName: String,
        @Json(name = "senderPhoto")
        senderPhoto: String,
        @Json(name = "receiverId")
        receiverId: String,
        @Json(name = "receiverName")
        receiverName: String,
        @Json(name = "receiverPhoto")
        receiverPhoto: String,
        @Json(name = "messageDate")
        messageDate: String,
        @Json(name = "message")
        message: String,
        @Json(name = "status")
        status: String
    ) : this(
        chatId,
        senderId,
        senderName,
        senderPhoto,
        receiverId,
        receiverName,
        receiverPhoto,
        messageDate,
        message,
        status,
        null
    )
}
