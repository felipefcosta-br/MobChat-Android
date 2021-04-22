package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.felipefcosta.mobchat.events.MessageEventListener
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
    var messagesList: MutableList<TextMessage> = emptyList<TextMessage>().toMutableList()
    var messages = MutableLiveData<List<TextMessage>>().apply { emptyList<TextMessage>() }
    var message = MutableLiveData<String>().apply { value = "" }


    fun initChat() {
        //val tokenTemp = chatHubRepository.getStorageToken() ?: return
        repository.addListener(this)

        Log.i("ProMIT", "Teste 01")
        if (profile == null && contactProfile == null)
            null
        getCurrentChat({ chat ->
            this.chat = chat
            chatId = this.chat.id
            ChatOnline.setCurrentChat(chat)
        }, {
        })

        if (profile.id == null && contactProfile.id == null)
            return

        getOldMessages(profile.id!!, contactProfile.id!!, { oldMessagesList ->
            messagesList = oldMessagesList.toMutableList()
            messages.postValue(messagesList.reversed().toList())

        }, {

        })


    }

    fun sendTextMessage() {

        if (profile.id == null || profile.name == null || contactProfile.id == null ||
            contactProfile.name == null || message == null
        ) return

        var messageDate = LocalDateTime.now()

        val textMessage = TextMessage(
            chatId,
            profile.id!!,
            profile.name!!,
            profile.photo!!,
            contactProfile.id!!,
            contactProfile.name!!,
            contactProfile.photo!!,
            messageDate.toString(),
            message.value.toString(),
            "online",
        )
        repository.sendTextMessage(profile.id!!, contactProfile.id!!, textMessage)
        message.value = ""
        addMessageToList(textMessage)
    }

    private fun addMessageToList(textMessage: TextMessage) {
        messagesList.add(textMessage)
        messages.postValue(messagesList.toList())
    }

    private fun getCurrentChat(sucess: (Chat) -> Unit, failure: () -> Unit) {
        repository.getChatByUserIdAndContactId(profile.id!!, contactProfile.id!!, { chat ->
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
        repository.getTextMessageByUserIdAndContactId(userId, contactId, {oldMessagesList ->
        if (!oldMessagesList.isNullOrEmpty()){
           success(oldMessagesList)
        }
        }, {
            failure()
        })
    }

    override fun onMessageReceivedListener(textMessage: TextMessage) {
        addMessageToList(textMessage)
        Log.i("ProMIT", "mensagem recebida: ${textMessage.toString()}")

    }
}