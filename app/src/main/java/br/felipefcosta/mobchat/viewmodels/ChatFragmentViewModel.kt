package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.felipefcosta.mobchat.api.SignalRHubService
import br.felipefcosta.mobchat.events.MessageEventListener
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.repositories.ChatRepository
import br.felipefcosta.mobchat.utils.ChatOnline
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class ChatFragmentViewModel(
    application: Application,
    private val repository: ChatRepository
) : AndroidViewModel(application), MessageEventListener {

    lateinit var chat: Chat
    lateinit var profile: Profile
    lateinit var contactProfile: Profile
    var messagesList: MutableList<TextMessage> = emptyList<TextMessage>().toMutableList()
    var messages = MutableLiveData<List<TextMessage>>().apply { emptyList<TextMessage>() }
    var message = MutableLiveData<String>().apply { value = "" }


    fun initChat() {
        //val tokenTemp = chatHubRepository.getStorageToken() ?: return
        if (profile == null && contactProfile == null)
            null
        getCurrentChat()
        repository.addListener(this)
    }

    fun sendTextMessage() {

        if (profile.id == null || profile.name == null || contactProfile.id == null ||
            contactProfile.name == null || message == null
        )
            return

        var messageDate = LocalDateTime.now()

        val textMessage = TextMessage(
            null,
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

    fun addMessageToList(textMessage: TextMessage) {
        messagesList.add(textMessage)
        messages.postValue(messagesList.toList())
    }

    private fun getCurrentChat(){
        var chat: Chat? = null
        repository.getChatByUserIdAndContactId(profile.id!!, contactProfile.id!!, {
            if (it != null){
                chat = it
            }else{
                chat = Chat(
                    profile.id!!,
                    "${profile.name} ${profile.surname}",
                    profile.photo!!,
                    contactProfile.id!!,
                    "${contactProfile.name} ${contactProfile.surname}",
                    contactProfile.photo!!,
                    "",
                    "",
                    "",
                    true
                )
            }
            this.chat = chat!!
            ChatOnline.setCurrentChat(chat!!)
        }, {
            chat = null
        })


    }

    override fun onMessageReceivedListener(textMessage: TextMessage) {
        addMessageToList(textMessage)
        Log.i("ProMIT", "mensagem recebida: ${textMessage.toString()}")

    }
}