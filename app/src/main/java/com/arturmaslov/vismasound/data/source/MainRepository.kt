package com.arturmaslov.vismasound.data.source

import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.local.LocalData
import com.arturmaslov.vismasound.data.source.local.LocalDataSource
import com.arturmaslov.vismasound.data.source.remote.RemoteData
import com.arturmaslov.vismasound.data.source.remote.RemoteDataSource
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

    override suspend fun fetchRemoteTrackList(
        genre: String?,
        amount: Int?
    ): List<Track>? {
        return mRemoteDataSource.fetchRemoteTrackList(genre, amount)
    }

}