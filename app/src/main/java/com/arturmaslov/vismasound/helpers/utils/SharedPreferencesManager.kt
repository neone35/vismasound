package com.arturmaslov.vismasound.helpers.utils

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE
    )
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()


    fun retrieveLastRefreshToken(): String? {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN, null)
    }

    fun saveSoundCloudTokenWithTime(value: String) {
        val currentDateAndTime = getCurrentDateAndTime()
        editor.putString(Constants.ACCESS_TOKEN, value)
        editor.putString(Constants.LAST_TOKEN_SAVE_DATE, currentDateAndTime)
        editor.apply()
    }

    fun hourPassedSinceSave(): Boolean {
        val lastSaveDate = sharedPreferences.getString(Constants.LAST_TOKEN_SAVE_DATE, null)

        return if (lastSaveDate != null) {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val savedDate = sdf.parse(lastSaveDate)
            val currentDate = Date()

            // Calculate the time difference in milliseconds
            val timeDifference = currentDate.time - savedDate.time
            // Convert milliseconds to hours
            val hoursPassed = timeDifference / (1000 * 60 * 60)
            // Check if an hour has passed
            hoursPassed >= 1
        } else {
            // If there is no last save date, consider an hour has passed
            true
        }
    }

    // Function to get the current date and time in the required format
    private fun getCurrentDateAndTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}