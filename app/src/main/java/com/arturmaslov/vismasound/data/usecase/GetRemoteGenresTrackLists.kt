package com.arturmaslov.vismasound.data.usecase

import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.MainRepository

class GetRemoteGenresTrackLists(
    private val mainRepo: MainRepository
) {
    suspend fun execute(genre: String): List<Track>? {
        return mainRepo.fetchRemoteTrackList(genre)
    }
}