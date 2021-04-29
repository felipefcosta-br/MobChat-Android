package br.felipefcosta.mobchat.models.repositories

import android.util.Log
import br.felipefcosta.mobchat.core.SignalRHubService
import br.felipefcosta.mobchat.models.dtos.TextMessageDto
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.services.ChatDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import br.felipefcosta.mobchat.presentation.events.ChatHubEventListener
import br.felipefcosta.mobchat.presentation.events.ChatMessageEventListener
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.lang.ref.WeakReference

class ChatListRepository(
    private val chatDataSource: ChatDataSource,
    private val tokenStorageManager: TokenStorageManager
): ChatHubEventListener {

    private var listener = WeakReference<ChatMessageEventListener>(null)

    init {
        SignalRHubService.addListener(this)
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
    fun addListener(listener: ChatMessageEventListener) {
        this.listener = WeakReference(listener)
    }

    override fun onMessageReceived(serializedMessage: String) {

        val moshiJson = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<TextMessageDto> = moshiJson.adapter(TextMessageDto::class.java)
        val textMessage = jsonAdapter.fromJson(serializedMessage)

        if (textMessage?.receiverId == null)
            null

        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            chatDataSource.getChatsByUserId(header, textMessage?.receiverId!!, { chatList ->
                Log.i("ProMIT", "teste chat list repository ${chatList.toString()}")
                listener.get()?.onChatReceiveMessageListener(chatList)
            }, {

            })
        }
    }


}