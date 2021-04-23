package br.felipefcosta.mobchat.models.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextMessageDto(
    @Json(name = "ChatId")
    var chatId: String?,
    @Json(name = "SenderId")
    var senderId: String,
    @Json(name = "SenderName")
    var senderName: String,
    @Json(name = "SenderPhoto")
    var senderPhoto: String,
    @Json(name = "ReceiverId")
    var receiverId: String,
    @Json(name = "ReceiverName")
    var receiverName: String,
    @Json(name = "ReceiverPhoto")
    var receiverPhoto: String,
    @Json(name = "MessageDate")
    var messageDate: String,
    @Json(name = "Message")
    var message: String,
    @Json(name = "Status")
    var status: String,
    @Json(name = "Id")
    val id: String?
) {

    @JvmOverloads
    constructor(
        @Json(name = "ChatId")
        chatId: String?,
        @Json(name = "SenderId")
        senderId: String,
        @Json(name = "SenderName")
        senderName: String,
        @Json(name = "SenderPhoto")
        senderPhoto: String,
        @Json(name = "ReceiverId")
        receiverId: String,
        @Json(name = "ReceiverName")
        receiverName: String,
        @Json(name = "ReceiverPhoto")
        receiverPhoto: String,
        @Json(name = "MessageDate")
        messageDate: String,
        @Json(name = "Message")
        message: String,
        @Json(name = "Status")
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