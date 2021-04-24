package br.felipefcosta.mobchat.core

import android.util.Log
import br.felipefcosta.mobchat.utils.Constants
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import java.io.InputStream

class StorageBlobApiService() {

    private lateinit var container: CloudBlobContainer

    fun startBlobService() {
        var account = CloudStorageAccount.parse(Constants.BLOB_STORAGE_CONNECTION_STRING)
        var blobClient = account.createCloudBlobClient()
        container = blobClient.getContainerReference("mobchatstoragecontainer")
        container.createIfNotExists()
        var containerPermission = BlobContainerPermissions()
        containerPermission.publicAccess = BlobContainerPublicAccessType.BLOB
        container.uploadPermissions(containerPermission)
    }

    suspend fun uploadFileBlobStorage(path: String, fileName: String): String {
        try {
            var blockBlobRef = container.getBlockBlobReference(fileName)

            blockBlobRef.uploadFromFile(path)
            return blockBlobRef.uri.toString()

        } catch (ex: Exception) {
            Log.i("ProMIT", ex.message.toString())
            throw java.lang.Exception()
        }
    }

    suspend fun uploadInputStreamBlobStorage(
        inputStream: InputStream,
        fileName: String,
        success: (String) -> Unit,
        failure: () -> Unit
    ) {
        try {
            var blockBlobRef = container.getBlockBlobReference(fileName)

            blockBlobRef.upload(inputStream, 56656565665)

            Log.i("ProMIT", "url: ${blockBlobRef.uri.toString()}")
            success(blockBlobRef.uri.toString())

        } catch (ex: Exception) {
            Log.i("ProMIT", ex.message.toString())
            failure()
            throw java.lang.Exception()
        }
    }
}