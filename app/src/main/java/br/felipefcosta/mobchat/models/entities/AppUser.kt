package br.felipefcosta.mobchat.models.entities

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppUser(
    @Json(name = "id")
    var id: String?,
    @Json(name = "userName")
    var username: String?,
    @Json(name = "email")
    var email: String?,
    @Json(name = "emailConfirmed")
    var emailConfirmed: Boolean?,
    @Json(name = "phoneNumber")
    var phoneNumber: String?,
    @Json(name = "phoneNumberConfirmed")
    var phoneNumberConfirmed: Boolean?,
    @Json(name = "lockoutEnabled")
    var lockoutEnabled: Boolean?,
    @Json(name = "twoFactorEnabled")
    var twoFactorEnabled: Boolean?,
    @Json(name = "accessFailedCount")
    var accessFailedCount: Int?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeValue(emailConfirmed)
        parcel.writeString(phoneNumber)
        parcel.writeValue(phoneNumberConfirmed)
        parcel.writeValue(lockoutEnabled)
        parcel.writeValue(twoFactorEnabled)
        parcel.writeValue(accessFailedCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppUser> {
        override fun createFromParcel(parcel: Parcel): AppUser {
            return AppUser(parcel)
        }

        override fun newArray(size: Int): Array<AppUser?> {
            return arrayOfNulls(size)
        }
    }
}
