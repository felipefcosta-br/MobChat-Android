package br.felipefcosta.mobchat.models.services

import android.hardware.camera2.CaptureFailure
import br.felipefcosta.mobchat.api.ChatApiService
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.ui.adapters.MessagesRecyclerViewAdapter
import kotlinx.coroutines.runBlocking

class ChatDataSource(private val chatApiService: ChatApiService) {

    fun getChat(header: String, id: String, success: (Chat) -> Unit, failure: () -> Unit) {
        runBlocking {
            val response = chatApiService.getChat(header, id)
            response.run {
                if (response.isSuccessful){
                    val chat = response.body() as Chat
                    success(chat)
                }else{
                    failure()
                }
            }
        }
    }

    fun getChatByUserIdAndContactId(
        header: String,
        userId: String,
        contactId: String,
        success: (Chat) -> Unit,
        failure: () -> Unit
    ) {
        runBlocking {
            val response = chatApiService.getChatByUserIdAndContactId(header, userId, contactId)
            response.run {
                if (response.isSuccessful){
                    if (response.body() != null){
                        val chat = response.body() as Chat
                        success(chat)
                    }else{
                        failure()
                    }
                }else{
                    failure()
                }
            }
        }
    }

    fun getChatsByUserId(
        header: String,
        userId: String,
        success: (List<Chat>) -> Unit,
        failure: () -> Unit
    ) {
        runBlocking {
            val response = chatApiService.getChatsByUserId(header, userId)
            response.run {
                var chatList: List<Chat> = emptyList()
                if (response.isSuccessful){
                    chatList = response.body() as List<Chat>
                    success(chatList)
                }else{
                    failure()
                }
            }
        }
    }
}