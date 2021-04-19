package br.felipefcosta.mobchat.ui.adapters

import android.view.View
import br.felipefcosta.mobchat.models.entities.Profile

interface MessagesRecyclerViewItemListener {
    fun recyclerViewItemClicked(view: View, userProfile: Profile, contactProfile: Profile)
}