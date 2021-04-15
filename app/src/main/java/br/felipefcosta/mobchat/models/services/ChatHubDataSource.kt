package br.felipefcosta.mobchat.models.services
import br.felipefcosta.mobchat.api.SignalRApiService
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.entities.Token
import com.microsoft.signalr.HubConnectionState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ChatHubDataSource(private val signalRApiService: SignalRApiService) {

    fun startConnection(token: Token){
        signalRApiService.startConnection(token)

    }
    fun connectToHub(userId: String){
        CoroutineScope(Dispatchers.IO).launch {
            val userIdGuid = UUID.fromString(userId)
            signalRApiService.connectToHub(userIdGuid)
        }
    }
    fun disconnect (){
        runBlocking {
            signalRApiService.disconnect()
        }
    }
    fun sendTextMessage(message: TextMessage){
        val userIdGuid = UUID.fromString(message.senderId)
        val contactIdGuid = UUID.fromString(message.receiverId)

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<TextMessage> = moshi.adapter(TextMessage::class.java)
        val serializedMessage = jsonAdapter.toJson(message)
        runBlocking {
            signalRApiService.sendTextMessage(userIdGuid, contactIdGuid, serializedMessage)
        }
    }

    fun removeUser(userId: String){
        val userIdGuid = UUID.fromString(userId)
        runBlocking {
            signalRApiService.removeUser(userIdGuid)
        }
    }
    fun isConnected() : HubConnectionState {
        return signalRApiService.isConnected()
    }
}