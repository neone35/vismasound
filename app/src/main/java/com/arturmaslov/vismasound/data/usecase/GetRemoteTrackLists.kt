package com.arturmaslov.vismasound.data.usecase

import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.MainRepository

class GetRemoteTrackLists(
    private val mainRepo: MainRepository
) {
    suspend fun executeMax(genre: String?): List<Track>? {
        return mainRepo.fetchRemoteTrackList(genre, null)
    }

    suspend fun execute5(genre: String?): List<Track>? {
        return mainRepo.fetchRemoteTrackList(genre, 5)
    }
}