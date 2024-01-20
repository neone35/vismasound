package com.arturmaslov.vismasound.helpers.utils

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import com.arturmaslov.vismasound.App
import com.arturmaslov.vismasound.models.Track
import com.google.gson.Gson
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object HelperUtils {

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    private fun getJsonFileBytes(
        dataList: List<Track?>
    ): ByteArray {
        val jsonString = Gson().toJson(dataList)
        return jsonString.toByteArray()
    }

    fun storePlainTextFileInMediaStore(dataList: List<Track?>, filename: String): Uri {
        val contentResolver = App.getAppContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, filename)
            put(MediaStore.Files.FileColumns.MIME_TYPE, "text/plain")
        }
        val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            ?: throw IOException("Failed to create plain text file")

        contentResolver.openOutputStream(uri)?.use { outputStream ->
            val jsonFileBytes = getJsonFileBytes(dataList)
            outputStream.write(jsonFileBytes)
            outputStream.flush()
        }

        return uri
    }
}