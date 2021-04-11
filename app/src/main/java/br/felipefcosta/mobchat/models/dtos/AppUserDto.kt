package br.felipefcosta.mobchat.models.dtos

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class AppUserDto(
    val id: String?,
    val userName: String?,
    val email: String?,
    val emailConfirmed: Boolean?,
    val phoneNumber: String?,
    val phoneNumberConfirmed: Boolean?,
    val lockoutEnabled: Boolean?,
    val twoFactorEnabled: Boolean?,
    val accessFailedCount: Int?
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
        parcel.writeString(userName)
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

    companion object CREATOR : Parcelable.Creator<AppUserDto> {
        override fun createFromParcel(parcel: Parcel): AppUserDto {
            return AppUserDto(parcel)
        }

        override fun newArray(size: Int): Array<AppUserDto?> {
            return arrayOfNulls(size)
        }
    }
}