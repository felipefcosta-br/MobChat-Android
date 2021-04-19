package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.ChatRepository
import java.lang.IllegalArgumentException

class ChatViewModelFactory(
    private val application: Application,
    private val repository: ChatRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatFragmentViewModel::class.java)){
            return ChatFragmentViewModel(application, repository) as T
        }
        throw IllegalArgumentException("This fragment is not compatible")
    }
}