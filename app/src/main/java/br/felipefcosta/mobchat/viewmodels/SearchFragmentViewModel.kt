package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn

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
        return profileRepository.getLocalProfile()
    }
}