package com.arturmaslov.vismasound.helpers.cache

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

    suspend fun checkIfHalfHourPassedSinceSave(): Boolean {
        val lastSaveMillis = tokenTimeCache.get()?.second

        return if (lastSaveMillis != null && lastSaveMillis > 0) {
            val currentMillis = System.currentTimeMillis()
            // Calculate the time difference in milliseconds
            val timeDifference = currentMillis - lastSaveMillis
            // Convert milliseconds to hours
            val minsPassed = timeDifference / (1000 * 60)
            // Check if an hour has passed
            minsPassed >= 30
        } else {
            // If there is no last save date, consider an hour has passed
            true
        }
    }
}