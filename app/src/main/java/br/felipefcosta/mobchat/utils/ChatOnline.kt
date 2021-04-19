package br.felipefcosta.mobchat.utils

import br.felipefcosta.mobchat.models.entities.Chat

object ChatOnline {
    var chat: Chat? = null

    fun getChatOnline(): Chat? {
        return chat
    }

    fun setCurrentChat(chat: Chat) {
        this.chat = chat
    }

    fun removeChatOnline() {
        this.chat = null
    }

}