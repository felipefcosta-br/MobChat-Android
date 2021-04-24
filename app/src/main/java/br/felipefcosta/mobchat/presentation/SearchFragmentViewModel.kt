package br.felipefcosta.mobchat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.ProfileRepository

class SearchFragmentViewModel(
    application: Application,
    private val profileRepository: ProfileRepository
) : AndroidViewModel(application) {

    var profiles = MutableLiveData<List<Profile>>().apply { emptyList<Profile>() }


    fun searchContacts(searchTxt: String) {
        profileRepository.searchProfile(searchTxt, { profilesList ->
            profiles.value = profilesList
        }, {

        })
    }

    fun getProfile() :Profile?{
        return profileRepository.getStorageProfile()
    }
}