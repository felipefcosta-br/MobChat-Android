package br.felipefcosta.mobchat.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.felipefcosta.mobchat.presentation.events.MessageEventListener
import br.felipefcosta.mobchat.models.dtos.TextMessageDto
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.repositories.ChatRepository
import br.felipefcosta.mobchat.utils.ChatOnline
import java.time.LocalDateTime

class ChatFragmentViewModel(
    application: Application,
    private val repository: ChatRepository,
) : AndroidViewModel(application), MessageEventListener {

    lateinit var chat: Chat
    lateinit var profile: Profile
    lateinit var contactProfile: Profile
    var chatId: String? = null
    var contactId: String? = null
    var contactName: String? = null
    var contactPhoto: String? = null
    var messagesList: MutableList<TextMessage> = emptyList<TextMessage>().toMutableList()
    var messages = MutableLiveData<List<TextMessage>>().apply { emptyList<TextMessage>() }
    var message = MutableLiveData<String>().apply { value = "" }


    fun initChat() {
        //val tokenTemp = chatHubRepository.getStorageToken() ?: return
        repository.addListener(this)

        if (chatId == null) {
            getCurrentChat({ chat ->
                this.chat = chat
                chatId = this.chat.id
                ChatOnline.setCurrentChat(chat)
            }, {
            })
        }

        if (profile.id == null && contactId == null)
            return

        getOldMessages(profile.id!!, contactId!!, { oldMessagesList ->
            messagesList = oldMessagesList.toMutableList()
            messages.postValue(messagesList.toList())

        }, {

        })


    }

    fun sendTextMessage() {

        if (profile.id == null || profile.name == null || contactId == null ||
            contactName == null || message == null
        ) return

        var messageDate = LocalDateTime.now()
        val userName = "${profile.name!!} ${profile.surname}"

        val textMessage = TextMessage(
            chatId,
            profile.id!!,
            userName,
            profile.photo!!,
            contactId!!,
            contactName!!,
            contactPhoto!!,
            messageDate.toString(),
            message.value.toString(),
            "online",
        )
        repository.sendTextMessage(profile.id!!, contactId!!, textMessage)
        message.value = ""
        addMessageToList(textMessage)
    }

    private fun addMessageToList(textMessage: TextMessage) {
        messagesList.add(textMessage)
        messages.postValue(messagesList.toList())
    }

    private fun getCurrentChat(sucess: (Chat) -> Unit, failure: () -> Unit) {
        repository.getChatByUserIdAndContactId(profile.id!!, contactId!!, { chat ->
            if (chat != null) {
                sucess(chat)
            }
            failure()
        }, {
        })
    }

    private fun getOldMessages(
        userId: String,
        contactId: String,
        success: (List<TextMessage>) -> Unit,
        failure: () -> Unit
    ) {
        repository.getTextMessageByUserIdAndContactId(userId, contactId, { oldMessagesList ->
            if (!oldMessagesList.isNullOrEmpty()) {
                success(oldMessagesList)
            }
        }, {
            failure()
        })
    }

    override fun onMessageReceivedListener(textMessageDto: TextMessageDto) {
        val textMessage = TextMessage(
            textMessageDto.chatId,
            textMessageDto.senderId,
            textMessageDto.senderName,
            textMessageDto.senderPhoto,
            textMessageDto.receiverId,
            textMessageDto.receiverName,
            textMessageDto.receiverPhoto,
            textMessageDto.messageDate,
            textMessageDto.message,
            textMessageDto.status,
            textMessageDto.id
        )
        addMessageToList(textMessage)
        Log.i("ProMIT", "mensagem recebida: ${textMessage.toString()}")

    }
}