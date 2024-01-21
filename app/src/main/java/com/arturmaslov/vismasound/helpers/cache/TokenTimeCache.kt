package com.arturmaslov.vismasound.helpers.cache

import android.content.SharedPreferences
import com.arturmaslov.vismasound.helpers.utils.Cache
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