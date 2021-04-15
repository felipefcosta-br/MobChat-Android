package br.felipefcosta.mobchat.api

import android.util.Log
import br.felipefcosta.mobchat.models.entities.Token
import br.felipefcosta.mobchat.utils.Constants
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.microsoft.signalr.TransportEnum
import io.reactivex.Single
import okhttp3.internal.wait
import java.util.*

class SignalRApiService {

    private lateinit var hubConnection: HubConnection

    fun startConnection(token: Token) {
        if (token.accessToken == null)
            return
        var single = Single.just(token.accessToken)

        hubConnection = HubConnectionBuilder.create(Constants.CHATHUB_API_URL)
            .withTransport(TransportEnum.ALL)
            .build()

        hubConnection.on("Conectado", {user, message ->

            Log.i("ProMIT", "Conectado $user")

        }, String::class.java, String::class.java)

        hubConnection.on("ReceiveMessage", {user, message ->}, String::class.java, String::class.java)
        hubConnection.start()
    }

    suspend fun connectToHub(userId: UUID){

        if(hubConnection.connectionState == HubConnectionState.CONNECTED){
            hubConnection.send("ConnectUser", userId.toString())
            Log.i("ProMIT", hubConnection.connectionState.toString())
        }else{
            //Log.i("ProMIT", hubConnection.connectionState.toString())
        }
    }
    suspend fun disconnect () {
        hubConnection.stop()
    }

    suspend fun sendTextMessage(userId: UUID, contactId: UUID, serializedMessage: String){

        hubConnection.invoke("SendTextMessage", userId, contactId, serializedMessage)

    }

    suspend fun removeUser(userId: UUID){
        hubConnection.send("DisconnectUser", userId)
    }
    fun isConnected() : HubConnectionState{
        return hubConnection.connectionState
    }

}