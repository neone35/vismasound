package com.arturmaslov.vismasound.source.local

import com.arturmaslov.vismasound.models.Track
import com.arturmaslov.vismasound.models.toDomainModel
import com.arturmaslov.vismasound.models.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocalDataSource(
    mLocalDatabase: LocalDatabase,
    private val mDispatcher: CoroutineDispatcher
) : LocalData {

    private val trackDao: TrackDao? = mLocalDatabase.trackDao

    // check for local data on startup and use it before making any remote requests
    override suspend fun getLocalTracks() =
        withContext(mDispatcher) {
            Timber.i("Running getLocalTracks()")
            val localTracks = trackDao?.getTracks()
            val trackList = localTracks?.map { it.toDomainModel() }
            if (localTracks != null) {
                Timber.i("Success: local tracks $localTracks retrieved")
            } else {
                Timber.i("Failure: unable to retrieve local tracks")
            }
            trackList
        }


    override suspend fun deleteTracks() =
        withContext(mDispatcher) {
            Timber.i("Running deleteTracks()")
            val deletedRows = trackDao?.deleteTrack()!!
            if (deletedRows != 0) {
                Timber.i("Success: all local track data deleted")
            } else {
                Timber.i("Failure: unable to delete local track data")
            }
        }

    override suspend fun insertTrack(track: Track): Long? =
        withContext(mDispatcher) {
            Timber.i("Running insertTrack()")
            val insertedId = trackDao?.insertTrack(track.toEntity())
            if (insertedId != null) {
                Timber.i("Success: track with id ${track.id} inserted")
            } else {
                Timber.i("Failure: unable to delete local track")
            }
            insertedId
        }

}

interface LocalData {
    suspend fun getLocalTracks(): List<Track>?
    suspend fun deleteTracks()
    suspend fun insertTrack(track: Track): Long?
}