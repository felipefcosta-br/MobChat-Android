package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.repositories.ChatHubRepository
import br.felipefcosta.mobchat.models.repositories.ChatRepository
import java.time.LocalDateTime

class ChatFragmentViewModel(
    application: Application,
    private val repository: ChatRepository,
    private val chatHubRepository: ChatHubRepository
) : AndroidViewModel(application) {

    lateinit var chat: Chat
    lateinit var profile: Profile
    lateinit var contactProfile: Profile


    fun initChat() {

    }

    fun SendTextMessage(message: String) {

        if (profile.id == null || profile.name == null || contactProfile.id == null ||
            contactProfile.name == null || message == null)
            return

        var messageDate = LocalDateTime.now()

        if (!chat.id.isNullOrBlank()) {

            val textMessage = TextMessage(
                profile.id!!,
                profile.name!!,
                profile.photo!!,
                contactProfile.id!!,
                contactProfile.name!!,
                contactProfile.photo!!,
                messageDate.toString(),
                message,
                chat.id,
                "online",
            )
            chatHubRepository.sendTextMessage(textMessage)

        }

    }
}