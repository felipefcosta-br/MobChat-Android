package br.felipefcosta.mobchat.presentation.events

interface ChatHubEventListener {
    fun onMessageReceived(serializedMessage: String)
}