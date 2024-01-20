package com.arturmaslov.vismasound.source

import com.arturmaslov.vismasound.models.Track
import com.arturmaslov.vismasound.source.local.LocalData
import com.arturmaslov.vismasound.source.local.LocalDataSource
import com.arturmaslov.vismasound.source.remote.RemoteData
import com.arturmaslov.vismasound.source.remote.RemoteDataSource
import timber.log.Timber

class MainRepository(
    private val mLocalDataSource: LocalDataSource,
    private val mRemoteDataSource: RemoteDataSource
) : LocalData, RemoteData {

    // watched from main thread for toast messages
    override val remoteResponse = mRemoteDataSource.remoteResponse

    init {
        Timber.d("Init MainRepository")
    }

    override suspend fun getLocalTracks(): List<Track>? {
        return mLocalDataSource.getLocalTracks()
    }

    override suspend fun deleteTracks() {
        return mLocalDataSource.deleteTracks()
    }

    override suspend fun insertTrack(track: Track): Long? {
        return mLocalDataSource.insertTrack(track)
    }

    override suspend fun fetchTrackResponse(): List<Track>? {
        return mRemoteDataSource.fetchTrackResponse()
    }

}