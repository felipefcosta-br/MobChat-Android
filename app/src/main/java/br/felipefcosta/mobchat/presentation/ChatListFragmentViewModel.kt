package br.felipefcosta.mobchat.presentation

import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.view.doOnLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatListRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import javax.security.auth.callback.Callback

class ChatListFragmentViewModel(
    application: Application,
    private val repository: ChatListRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository

) : AndroidViewModel(application) {
    var profile: Profile? = null
    var chatList: MutableList<Chat> = emptyList<Chat>().toMutableList()
    var chats = MutableLiveData<List<Chat>>().apply { emptyList<Chat>() }

    init {
        profile = profileRepository.getStorageProfile()
        if (profile == null)
            getUserProfile({
                profile = it
            }, {
                //return@getUserProfile
            })
    }

    fun getUserProfile(success: (Profile) -> Unit, failure: () -> Unit) {
        val jwtToken = authRepository.decodeToken()
        if (jwtToken !== null) {
            profileRepository.getProfileByAccountId(jwtToken.jwtPayload.sub, {
                profile = it
                if (profile != null) {
                    profileRepository.storeLocalProfile(profile!!)
                    getListOfChats(profile?.id!!, { list ->
                        chatList = list.toMutableList()
                        chats.postValue(chatList.toList())
                    }, {
                        failure()
                    })
                    success(it)
                }
            }, {
                failure()
            })
        } else {
            failure()
        }

    }

    private fun getListOfChats(userId: String, success: (List<Chat>) -> Unit, failure: () -> Unit) {
        repository.getChatsByUserId(userId, {
            Log.i("ProMIT", "teste chat list ${it.toString()}")
            success(it)
        }, {
            failure()
        })
    }


}