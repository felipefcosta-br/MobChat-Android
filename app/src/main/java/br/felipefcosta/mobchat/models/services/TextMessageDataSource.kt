package br.felipefcosta.mobchat.models.services

import br.felipefcosta.mobchat.api.TextMessageApiService
import br.felipefcosta.mobchat.models.entities.TextMessage
import kotlinx.coroutines.runBlocking

class TextMessageDataSource(private val textMessageApiService: TextMessageApiService) {

    fun getTextMessagesByChatId(
        header: String,
        chatId: String,
        success: (List<TextMessage>) -> Unit,
        failure: () -> Unit
    ) {
        runBlocking {
            val response = textMessageApiService.getTextMessagesByChatId(header, chatId)
            response.run {
                if (response.isSuccessful) {
                    val textMessageList = response?.body() as List<TextMessage>
                    success(textMessageList)
                } else {
                    failure()
                }
            }

        }

    }

    fun getTextMessageByUserIdAndContactId(
        header: String,
        userId: String,
        contactId: String,
        success: (List<TextMessage>) -> Unit,
        failure: () -> Unit
    ) {
        runBlocking {
            val response =
                textMessageApiService.getTextMessageByUserIdAndContactId(header, userId, contactId)
            response.run {
                if(response.isSuccessful){
                    val textMessageList = response?.body() as List<TextMessage>
                    success(textMessageList)
                }else{
                    failure()
                }
            }

        }
    }

}