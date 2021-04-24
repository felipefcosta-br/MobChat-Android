package br.felipefcosta.mobchat.presentation.events

interface ChatHubEventListener {
    fun onMessageReceived(message: String)
}