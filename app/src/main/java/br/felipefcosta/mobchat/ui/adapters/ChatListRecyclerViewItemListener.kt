package br.felipefcosta.mobchat.ui.adapters

import android.view.View
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.Profile

interface ChatListRecyclerViewItemListener {

    fun recyclerViewItemClicked(view: View, chat: Chat)
}