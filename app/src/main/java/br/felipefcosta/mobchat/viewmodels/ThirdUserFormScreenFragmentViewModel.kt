package br.felipefcosta.mobchat.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.repositories.StorageBlobRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset

class ThirdUserFormScreenFragmentViewModel(
    application: Application,
    private val repository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val storageBlobRepository: StorageBlobRepository
) : AndroidViewModel(application) {

    lateinit var profile: Profile
    lateinit var photoUri: Uri

    fun addUserProfile(context: Context, success: (Profile) -> Unit, failure: () -> Unit) {

        if (this::photoUri.isInitialized && photoUri != null) {
            var inputStream = context.contentResolver.openInputStream(photoUri)
            if (inputStream != null) {
                val photoName = "photo${profile.accountId.hashCode()}.jpg"
                storageBlobRepository.addInputStream(inputStream, photoName, {
                    profile.photo = it
                    addProfile({profile ->
                        success(profile)
                    }, {
                        failure()
                    })
                }, {
                    failure()
                })
            }
        }else{
            addProfile({profile ->
                success(profile)
            }, {
                failure()
            })
        }
    }

    fun addProfile(success: (Profile) -> Unit, failure: () -> Unit) {
        authRepository.getToken({ token ->
            repository.addProfile(profile, token, {
                success(it)
            }, {
                failure()
            })
        }, {
            failure()
        })

    }


    fun createImageFile(context: Context, userId: String): File {
        val timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z"))
        val imageName = "photo${timeStamp}${userId.hashCode()}"
        val storageDir = context.externalCacheDir
        return File.createTempFile(imageName, ".jpg", storageDir)
    }

}