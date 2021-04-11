package br.felipefcosta.mobchat.models.repositories

import android.content.Context
import br.felipefcosta.mobchat.models.services.StorageBlobDataSource
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneOffset

class StorageBlobRepository(
    private val storageBlobDataSource: StorageBlobDataSource
) {
    fun addFile(filePath: String, fileName: String): String? {
        return storageBlobDataSource.addFile(filePath, fileName)
    }

    fun addInputStream(
        inputStream: InputStream,
        fileName: String,
        success: (String) -> Unit,
        failure: () -> Unit
    ) {
        storageBlobDataSource.addInputStream(inputStream, fileName, {
            success(it)
        }, {
            failure()
        })
    }
}