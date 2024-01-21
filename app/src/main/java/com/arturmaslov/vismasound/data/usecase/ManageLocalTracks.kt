package com.arturmaslov.vismasound.data.usecase

import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.MainRepository

class ManageLocalTracks(
    private val mainRepo: MainRepository
) {
    suspend fun executeInsertOne(track: Track): Long? {
        return mainRepo.insertTrack(track)
    }

    suspend fun executeGetAll(): List<Track>? {
        return mainRepo.getLocalTracks()
    }

    suspend fun deleteOne(track: Track): Int? {
        return mainRepo.deleteTrack(track)
    }
}