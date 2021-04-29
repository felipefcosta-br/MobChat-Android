package br.felipefcosta.mobchat.presentation.events

import br.felipefcosta.mobchat.models.entities.Chat


interface ChatMessageEventListener {
    fun onChatReceiveMessageListener(chatList: List<Chat>)
}