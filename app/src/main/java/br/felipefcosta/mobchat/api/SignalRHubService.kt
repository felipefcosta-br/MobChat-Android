package br.felipefcosta.mobchat.api

import android.annotation.SuppressLint
import android.util.Log
import br.felipefcosta.mobchat.events.ChatHubEventListener
import br.felipefcosta.mobchat.models.entities.TextMessage
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.utils.Constants
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.microsoft.signalr.TransportEnum
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.Single
import java.lang.ref.WeakReference
import java.util.*

object SignalRHubService {

    private lateinit var hubConnection: HubConnection

    private var listener = WeakReference<ChatHubEventListener>(null)

    fun startHubConnection(token: Token, userId: UUID){

        if (token.accessToken == null)
            return
        var single = Single.just("bearer ${token.accessToken.toString()}")

        hubConnection = HubConnectionBuilder.create(Constants.CHATHUB_API_URL)
            .withTransport(TransportEnum.LONG_POLLING)
            .build()

        hubConnection.on("Conectado", { user, message ->

            Log.i("ProMIT", "Conectado $user")

        }, String::class.java, String::class.java)

        hubConnection.on("ReceiveMessage", { user, message ->

            Log.i("ProMIT", "Mensagem recebida $user  $message")
            listener.get()?.onMessageReceived(message)

        }, String::class.java, String::class.java)

    }

    @SuppressLint("CheckResult")
    suspend fun connectToHub(userId: String){

        val userIdGuid = UUID.fromString(userId)

        hubConnection.start().repeat(1000).subscribe({
            hubConnection.send("ConnectUser", userIdGuid)
            Log.i("ProMIT", "Connect teste: ${hubConnection.connectionState.toString()}")
        },{
            Log.i("ProMIT", "Connection error: ${it.message.toString()}")

        })

       /* if(hubConnection.connectionState == HubConnectionState.CONNECTED){

            hubConnection.send("ConnectUser", userIdGuid)
            Log.i("ProMIT", "Connect teste: ${hubConnection.connectionState.toString()}")

        }else{
            //Log.i("ProMIT", hubConnection.connectionState.toString())
        }*/

    }

    suspend fun disconnect () {
        hubConnection.stop()
    }

    suspend fun sendTextMessage(userId: String, contactId: String, textMessage: TextMessage){

        val userIdGuid = UUID.fromString(userId)
        val contactIdGuid = UUID.fromString(contactId)

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<TextMessage> = moshi.adapter(TextMessage::class.java)
        val textMessageJson = jsonAdapter.toJson(textMessage)

        hubConnection.invoke("SendTextMessage", userIdGuid, contactIdGuid, textMessageJson)

    }

    suspend fun removeUser(userId: UUID){
        hubConnection.send("DisconnectUser", userId)
    }

    fun getConnectionState() : HubConnectionState {
        return hubConnection.connectionState
    }
    fun isConnected() : Boolean {
        if (hubConnection.connectionState == HubConnectionState.CONNECTED)
            return true

        return false
    }

    fun addListener(listener: ChatHubEventListener){
        this.listener = WeakReference(listener)
    }
}