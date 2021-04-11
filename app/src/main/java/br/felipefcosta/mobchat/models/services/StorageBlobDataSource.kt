package br.felipefcosta.mobchat.models.services

import android.hardware.camera2.CaptureFailure
import android.util.Log
import br.felipefcosta.mobchat.api.StorageBlobApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class StorageBlobDataSource(private val storageBlobApiService: StorageBlobApiService) {

    fun addFile(filePath: String, fileName: String): String? {
        var fileUrl: String? = null
        CoroutineScope(Dispatchers.IO).launch {
            storageBlobApiService.startBlobService()
            fileUrl = storageBlobApiService.uploadFileBlobStorage(filePath, fileName)
        }
        return fileUrl
    }

    fun addInputStream(
        inputStream: InputStream,
        fileName: String,
        success: (String) -> Unit,
        failure: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            storageBlobApiService.startBlobService()
            storageBlobApiService.uploadInputStreamBlobStorage(inputStream, fileName, {

                    Log.i("ProMIT", "url: ${it.toString()}")
                    success(it)

            }, {
                failure()
            })

        }
    }
}