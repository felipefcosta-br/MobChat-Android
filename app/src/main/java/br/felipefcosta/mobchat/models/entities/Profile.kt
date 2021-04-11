package br.felipefcosta.mobchat.models.entities

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
    @Json(name = "id")
    var id: String?,
    @Json(name = "accountId")
    var accountId: String?,
    @Json(name = "name")
    var name: String?,
    @Json(name = "surname")
    var surname: String?,
    @Json(name = "gender")
    var gender: String?,
    @Json(name = "city")
    var city: String?,
    @Json(name = "country")
    var country: String?,
    @Json(name = "photo")
    var photo: String?,
    @Json(name = "thumbnail")
    var thumbnail: String?,
    @Json(name = "userName")
    var userName: String?,
    @Json(name = "email")
    var email: String?,
    @Json(name = "mobileNumber")
    var mobilePhone: String?,
    @Json(name = "isVisibleEmail")
    var isVisibleEmail: Boolean,
    @Json(name = "isVisiblePhone")
    var isVisiblePhone: Boolean,
    @Json(name = "birthDate")
    val birthDate: String?,
    @Json(name = "registration")
    val registrationDate: String?

): Parcelable {
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(accountId)
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(gender)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeString(photo)
        parcel.writeString(thumbnail)
        parcel.writeString(userName)
        parcel.writeString(email)
        parcel.writeString(mobilePhone)
        parcel.writeByte(if (isVisibleEmail) 1 else 0)
        parcel.writeByte(if (isVisiblePhone) 1 else 0)
        parcel.writeString(birthDate)
        parcel.writeString(registrationDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Profile> {
        override fun createFromParcel(parcel: Parcel): Profile {
            return Profile(parcel)
        }

        override fun newArray(size: Int): Array<Profile?> {
            return arrayOfNulls(size)
        }
    }
}