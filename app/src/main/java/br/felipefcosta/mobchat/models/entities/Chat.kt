package br.felipefcosta.mobchat.models.entities

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Chat(
    @Json(name = "firstMemberId")
    var firstMemberId: String?,
    @Json(name = "firstMemberName")
    var firstMemberName: String?,
    @Json(name = "firstMemberPhoto")
    var firstMemberPhoto: String?,
    @Json(name = "secondMemberId")
    var secondMemberId: String?,
    @Json(name = "secondMemberName")
    var secondMemberName: String?,
    @Json(name = "secondMemberPhoto")
    var secondMemberPhoto: String?,
    @Json(name = "firstMessageDate")
    var firstMessageDate: String?,
    @Json(name = "lastMessageDate")
    var lastMessageDate: String?,
    @Json(name = "status")
    var status: String?,
    @Json(name = "isOnline")
    var isOnline: Boolean,
    @Json(name = "unreadMessages")
    var unreadMessages: Int,
    @Json(name = "id")
    val id: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

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
        isOnline: Boolean,
        @Json(name = "unreadMessages")
        unreadMessages: Int
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
        unreadMessages,
        id = null
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstMemberId)
        parcel.writeString(firstMemberName)
        parcel.writeString(firstMemberPhoto)
        parcel.writeString(secondMemberId)
        parcel.writeString(secondMemberName)
        parcel.writeString(secondMemberPhoto)
        parcel.writeString(firstMessageDate)
        parcel.writeString(lastMessageDate)
        parcel.writeString(status)
        parcel.writeByte(if (isOnline) 1 else 0)
        parcel.writeInt(unreadMessages)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}
