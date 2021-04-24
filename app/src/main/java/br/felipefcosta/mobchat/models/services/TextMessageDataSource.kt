package br.felipefcosta.mobchat.models.services

import android.util.Log
import br.felipefcosta.mobchat.core.TextMessageApiService
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
                    val textMessagesList = response?.body() as List<TextMessage>


                    success(textMessagesList)
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
                    val textMessagesList = response?.body() as List<TextMessage>
                    success(textMessagesList)
                    Log.i("ProMIT", textMessagesList.toString())
                }else{
                    failure()
                    Log.i("ProMIT", "Error getTextMessagesByChatId")
                }
            }

        }
    }

}