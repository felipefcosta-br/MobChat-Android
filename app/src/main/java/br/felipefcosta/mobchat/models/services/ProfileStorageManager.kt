package br.felipefcosta.mobchat.models.services

import android.content.Context
import br.felipefcosta.mobchat.models.entities.Profile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class ProfileStorageManager(val context: Context, val encryptionManager: EncryptionManager) {
    private var profile: Profile? = null
    private val applicationName = "br.felipefcosta.mobchat"
    private val key = "PROFILE_STATE"
    private val sharedPref = context.getSharedPreferences(applicationName, Context.MODE_PRIVATE)

    fun getProfile(): Profile? {

        if (profile != null)
            return profile

        loadProfile()
        return profile
    }

    fun saveProfile(newProfile: Profile) {
        profile = newProfile
        storeProfile()
    }

    fun removeProfile() {
        profile = null
        sharedPref.edit().remove(key).apply()
    }

    private fun loadProfile() {

        if (profile != null)
            return

        val encryptedData = sharedPref.getString(key, "")
        if (encryptedData.isNullOrBlank()) {
            return
        }

        var decryptedData: String?
        try {

            decryptedData = encryptionManager.decrypt(encryptedData)

            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Profile> = moshi.adapter(Profile::class.java)
            profile = jsonAdapter.fromJson(decryptedData)


        } catch (ex: Exception) {
            throw IllegalArgumentException()

        }
    }

    private fun storeProfile() {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Profile> = moshi.adapter(Profile::class.java)
        val profileJson = jsonAdapter.toJson(profile)
        val encryptedData = encryptionManager.encrypt(profileJson)
        sharedPref.edit().putString(key, encryptedData).apply()
    }
}