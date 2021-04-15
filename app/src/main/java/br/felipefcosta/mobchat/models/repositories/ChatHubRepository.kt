package br.felipefcosta.mobchat.models.repositories

import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.models.services.ChatHubDataSource
import com.microsoft.signalr.HubConnectionState

class ChatHubRepository(private val chatHubDataSource: ChatHubDataSource) {

    fun startConnection(token: Token){
        chatHubDataSource.startConnection(token)
    }
    fun connectToHub(userId: String){
        var sended = false
        while (!sended){
            if (isConnected() == HubConnectionState.CONNECTED){
                chatHubDataSource.connectToHub(userId)
                sended = true
            }
        }

    }
    fun disconnect (){
        chatHubDataSource.disconnect()
    }
    fun sendTextMessage(message: TextMessage){
        chatHubDataSource.sendTextMessage(message)
    }
    fun removeUser(userId: String){
        chatHubDataSource.removeUser(userId)
    }
    fun isConnected() : HubConnectionState {
        return chatHubDataSource.isConnected()
    }
}