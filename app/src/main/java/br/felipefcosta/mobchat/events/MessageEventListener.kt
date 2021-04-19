package br.felipefcosta.mobchat.events

import br.felipefcosta.mobchat.models.entities.TextMessage

interface MessageEventListener {
    fun onMessageReceivedListener(textMessage: TextMessage)
}