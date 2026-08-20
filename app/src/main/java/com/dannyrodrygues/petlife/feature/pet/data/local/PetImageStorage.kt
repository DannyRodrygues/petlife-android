package com.dannyrodrygues.petlife.feature.pet.data.local

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

object PetImageStorage {

    fun saveImage(
        context: Context,
        sourceUri: Uri,
    ): String? {
        return try {
            val imagesDirectory = File(
                context.filesDir,
                "pet_images",
            )

            if (!imagesDirectory.exists()) {
                imagesDirectory.mkdirs()
            }

            val imageFile = File(
                imagesDirectory,
                "${UUID.randomUUID()}.jpg",
            )

            context.contentResolver
                .openInputStream(sourceUri)
                ?.use { inputStream ->

                    imageFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                ?: return null

            Uri.fromFile(imageFile).toString()

        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }
}
