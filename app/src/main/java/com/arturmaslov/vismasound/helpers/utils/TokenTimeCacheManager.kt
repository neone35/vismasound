package com.arturmaslov.vismasound.helpers.utils

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher

abstract class TokenTimeCache(dispatcher: CoroutineDispatcher) :
    Cache<Pair<String?, Long>>(dispatcher)

class TokenTimeCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : TokenTimeCache(dispatcher) {

    override suspend fun getFromStorage(): Pair<String?, Long> {
        val token = sharedPreferences.getString(REFRESH_TOKEN, null)
        val date = sharedPreferences.getLong(LAST_TOKEN_SAVE_TIME, 0)
        return Pair(token, date)
    }

    override suspend fun saveInStorage(value: Pair<String?, Long>) {
        sharedPreferences.edit().putString(REFRESH_TOKEN, value.first).apply()
        sharedPreferences.edit().putLong(LAST_TOKEN_SAVE_TIME, value.second).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putString(REFRESH_TOKEN, null).apply()
        sharedPreferences.edit().putLong(LAST_TOKEN_SAVE_TIME, 0).apply()
    }

    companion object {
        const val REFRESH_TOKEN = "refresh_token"
        const val LAST_TOKEN_SAVE_TIME = "last_token_save_time"
    }

}

class TokenTimeCacheManager(
    private val tokenTimeCache: TokenTimeCache
) {

    suspend fun retrieveLastRefreshToken(): String? {
        return tokenTimeCache.get()?.first
    }

    suspend fun saveSoundCloudTokenWithTime(token: String) {
        val tokenWithTime = Pair(token, System.currentTimeMillis())
        tokenTimeCache.set(tokenWithTime)
    }

    suspend fun checkIfHourPassedSinceSave(): Boolean {
        val lastSaveMillis = tokenTimeCache.get()?.second

        return if (lastSaveMillis != null && lastSaveMillis > 0) {
            val currentMillis = System.currentTimeMillis()
            // Calculate the time difference in milliseconds
            val timeDifference = currentMillis - lastSaveMillis
            // Convert milliseconds to hours
            val hoursPassed = timeDifference / (1000 * 60 * 60)
            // Check if an hour has passed
            hoursPassed >= 1
        } else {
            // If there is no last save date, consider an hour has passed
            true
        }
    }
}