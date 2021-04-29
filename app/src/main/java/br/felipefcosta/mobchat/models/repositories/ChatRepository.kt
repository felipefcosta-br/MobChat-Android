package br.felipefcosta.mobchat.models.repositories

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import br.felipefcosta.mobchat.core.SignalRHubService
import br.felipefcosta.mobchat.presentation.events.ChatHubEventListener
import br.felipefcosta.mobchat.presentation.events.MessageEventListener
import br.felipefcosta.mobchat.models.dtos.TextMessageDto
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.services.ChatDataSource
import br.felipefcosta.mobchat.models.services.ChatStorageManager
import br.felipefcosta.mobchat.models.services.TextMessageDataSource
import br.felipefcosta.mobchat.models.services.TokenStorageManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import java.util.*

class ChatRepository(
    private val chatDataSource: ChatDataSource,
    private val textMessageDataSource: TextMessageDataSource,
    private val tokenStorageManager: TokenStorageManager,
    private val chatStorageManager: ChatStorageManager
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

    fun getTextMessagesByChatId(
        chatId: String, userId: String,
        contactId: String,
        success: (List<TextMessage>) -> Unit,
        failure: () -> Unit
    ) {
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            textMessageDataSource.getTextMessagesByChatId(header, chatId, { messageList ->
                success(messageList)
            }, {
                failure()
            })
        }
    }

    fun getTextMessageByUserIdAndContactId(
        userId: String,
        contactId: String,
        success: (List<TextMessage>) -> Unit,
        failure: () -> Unit
    ) {
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            val header = "bearer ${token?.accessToken.toString()}"
            textMessageDataSource.getTextMessageByUserIdAndContactId(
                header,
                userId,
                contactId,
                { messageList ->
                    success(messageList)
                },
                {
                    failure()
                })
        }
    }

    fun checkHubConnection(userId: String){
        val token = tokenStorageManager.getToken()
        if (!token?.accessToken.isNullOrBlank()) {
            if (!SignalRHubService.isHubConnectionInitialized()){
                val userIdGuid = UUID.fromString(userId)
                runBlocking {
                    SignalRHubService.startHubConnection(token!!, userIdGuid)
                    SignalRHubService.connectToHub(userId)
                }


            }
        }

    }

    fun sendTextMessage(senderId: String, receiverId: String, textMessage: TextMessage) {

        runBlocking {
            SignalRHubService.sendTextMessage(senderId, receiverId, textMessage)
        }


    }

    override fun onMessageReceived(serializedMessage: String) {

        val moshiJson = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<TextMessageDto> = moshiJson.adapter(TextMessageDto::class.java)
        val textMessage = jsonAdapter.fromJson(serializedMessage)

        val chat = chatStorageManager.getChat()
        if ((textMessage != null) && (textMessage?.senderId == chat?.firstMemberId || textMessage?.senderId == chat?.secondMemberId) &&
            (textMessage?.receiverId == chat?.firstMemberId || textMessage?.receiverId == chat?.secondMemberId)){

                listener.get()?.onMessageReceivedListener(textMessage)
        }

    }

    fun storeChat(chat: Chat){
        chatStorageManager.saveChat(chat)
    }

    fun removeChat(){
        chatStorageManager.removeChat()
    }

    fun addListener(listener: MessageEventListener) {
        this.listener = WeakReference(listener)
    }


}