package br.felipefcosta.mobchat.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import br.felipefcosta.mobchat.models.entities.Chat
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatListRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.presentation.events.ChatMessageEventListener

class ChatListFragmentViewModel(
    application: Application,
    private val repository: ChatListRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository

) : AndroidViewModel(application), ChatMessageEventListener, LifecycleObserver {
    var profile: Profile? = null
    var chatList: MutableList<Chat> = emptyList<Chat>().toMutableList()
    var chats = MutableLiveData<List<Chat>>().apply { emptyList<Chat>() }



    fun initChatListFragmentViewModel(success: (Profile) -> Unit, failure: () -> Unit){
        profile = profileRepository.getStorageProfile()
        if (profile == null) {

            getUserProfile({
                if (it != null){
                    profile = it
                    success(profile!!)
                    repository.checkHubConnection(profile?.id!!)
                }

            }, {
                //return@getUserProfile
            })
        }else{
            success(profile!!)
            repository.checkHubConnection(profile?.id!!)
            startChatListRecyclerView()
        }



        repository.addListener(this)
    }


    private fun getUserProfile(success: (Profile) -> Unit, failure: () -> Unit) {
        val jwtToken = authRepository.decodeToken()
        if (jwtToken !== null) {
            profileRepository.getProfileByAccountId(jwtToken.jwtPayload.sub, {
                profile = it
                if (profile != null) {
                    profileRepository.storeLocalProfile(profile!!)
                    success(profile!!)
                    startChatListRecyclerView()
                }
            }, {
                failure()
            })
        } else {
            failure()
        }

    }

    private fun startChatListRecyclerView(){
        getListOfChats(profile?.id!!, { list ->
            chatList = list.toMutableList()
            chats.postValue(chatList.toList())
        }, {
        })
    }

    private fun getListOfChats(userId: String, success: (List<Chat>) -> Unit, failure: () -> Unit) {
        repository.getChatsByUserId(userId, {
            success(it)
        }, {
            failure()
        })
    }

    override fun onChatReceiveMessageListener(chatList: List<Chat>) {
        this.chatList = chatList.toMutableList()
        chats.postValue(chatList.toList())
    }

    /*override fun updateChatList(chatList: List<Chat>) {
        Log.i("ProMIT", "teste chat list view model ${chatList.toString()}")
        this.chatList = chatList.toMutableList()
        chats.postValue(chatList.toList())
    }*/


}