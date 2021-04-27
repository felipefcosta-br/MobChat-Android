package br.felipefcosta.mobchat.models.services

import android.content.Context
import br.felipefcosta.mobchat.models.entities.Chat
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class ChatStorageManager(val context: Context, val encryptionManager: EncryptionManager) {
    private var chat: Chat? = null
    private val applicationName = "br.felipefcosta.mobchat"
    private val key = "CHAT_STATE"
    private val sharedPref = context.getSharedPreferences(applicationName, Context.MODE_PRIVATE)

    fun getChat(): Chat? {

        if (chat != null)
            return chat

        loadChat()
        return chat
    }

    fun saveChat(newChat: Chat) {
        chat = newChat
        storeChat()
    }

    fun removeChat() {
        chat = null
        sharedPref.edit().remove(key).apply()
    }

    private fun loadChat() {

        if (chat != null)
            return

        val encryptedData = sharedPref.getString(key, "")
        if (encryptedData.isNullOrBlank()) {
            return
        }

        var decryptedData: String?
        try {

            decryptedData = encryptionManager.decrypt(encryptedData)

            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Chat> = moshi.adapter(Chat::class.java)
            chat = jsonAdapter.fromJson(decryptedData)


        } catch (ex: Exception) {
            throw IllegalArgumentException()

        }
    }

    private fun storeChat() {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Chat> = moshi.adapter(Chat::class.java)
        val chatJson = jsonAdapter.toJson(chat)
        val encryptedData = encryptionManager.encrypt(chatJson)
        sharedPref.edit().putString(key, encryptedData).apply()
    }
}