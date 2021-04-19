package br.felipefcosta.mobchat.events

import br.felipefcosta.mobchat.models.entities.TextMessage

interface ChatHubEventListener {
    fun onMessageReceived(message: String)
}