package br.felipefcosta.mobchat.presentation

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import br.felipefcosta.mobchat.models.entities.Profile
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.repositories.StorageBlobRepository
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

    fun updateProfile(context: Context, success: (Boolean) -> Unit, failure: () -> Unit) {
        var inputStream = context.contentResolver.openInputStream(photoUri)
        if (inputStream != null) {
            val photoName = "photo${profile.accountId.hashCode()}.jpg"
            storageBlobRepository.addInputStream(inputStream, photoName, { it ->
                profile.photo = it
                repository.updateProfile(profile, { response ->
                    success(response)
                }, {
                    failure()
                })
            }, {
                failure()
            })
        }else{
            success(true)
        }
    }

    fun createImageFile(context: Context, userId: String): File {
        val timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z"))
        val imageName = "photo${timeStamp}${userId.hashCode()}"
        val storageDir = context.externalCacheDir
        return File.createTempFile(imageName, ".jpg", storageDir)
    }

}