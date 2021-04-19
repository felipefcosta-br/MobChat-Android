package br.felipefcosta.mobchat.models.repositories

import android.util.Log
import br.felipefcosta.mobchat.api.SignalRHubService
import br.felipefcosta.mobchat.events.ChatHubEventListener
import br.felipefcosta.mobchat.events.MessageEventListener
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.services.ChatDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import java.time.LocalDateTime

class ChatRepository(
    private val chatDataSource: ChatDataSource, private val tokenStorageManager: TokenStorageManager
) : ChatHubEventListener {

    private var listener = WeakReference<MessageEventListener>(null)

    init {
        SignalRHubService.addListener(this)
    }

    fun getChat(id: String, success: (Chat) -> Unit, failure: () -> Unit) {
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            chatDataSource.getChat(header, id, { chat ->
                success(chat)
            }, {
                failure()
            })

        }
    }

    fun getChatByUserIdAndContactId(
        userId: String,
        contactId: String,
        success: (Chat) -> Unit,
        failure: () -> Unit
    ) {
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            chatDataSource.getChatByUserIdAndContactId(header, userId, contactId, { chat ->
                success(chat)
            }, {
                failure()
            })
        }

    }

    fun getChatsByUserId(userId: String, success: (List<Chat>) -> Unit, failure: () -> Unit) {
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            chatDataSource.getChatsByUserId(header, userId, { chatList ->
                success(chatList)
            }, {
                failure()
            })
        }
    }


    fun sendTextMessage(senderId: String, receiverId: String, textMessage: TextMessage) {

        runBlocking {
            SignalRHubService.sendTextMessage(senderId, receiverId, textMessage)
        }


    }

    override fun onMessageReceived(serializedMessage: String) {
        Log.i("ProMIT", "mensagem recebida: $serializedMessage")
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<TextMessage> = moshi.adapter(TextMessage::class.java)
        val textMessage = jsonAdapter.fromJson(serializedMessage)

        if (textMessage != null)
            listener.get()?.onMessageReceivedListener(textMessage)
    }

    fun addListener(listener: MessageEventListener) {
        this.listener = WeakReference(listener)
    }

}