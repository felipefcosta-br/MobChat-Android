package br.felipefcosta.mobchat.ui.adapters

import android.view.View
import br.felipefcosta.mobchat.models.entities.Profile

interface RecyclerViewItemListener {

    fun recyclerViewItemClicked(view: View, contactProfile: Profile)
}