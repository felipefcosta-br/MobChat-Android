package br.felipefcosta.mobchat.presentation.events

import br.felipefcosta.mobchat.models.dtos.TextMessageDto

interface MessageEventListener {
    fun onMessageReceivedListener(textMessage: TextMessageDto)
}