package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import java.lang.IllegalArgumentException

class SearchViewModelFactory(
    private val application: Application,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchFragmentViewModel::class.java)){
            return SearchFragmentViewModel(application, profileRepository) as T
        }
        throw IllegalArgumentException("This fragment is not compatible")
    }
}