package com.arturmaslov.vismasound.source.remote

import com.arturmaslov.vismasound.helpers.extensions.PublishFlow
import com.arturmaslov.vismasound.models.Track
import com.arturmaslov.vismasound.models.TrackDto
import com.arturmaslov.vismasound.models.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import retrofit2.Call
import timber.log.Timber

class RemoteDataSource(
    private val api: Api,
    private val mDispatcher: CoroutineDispatcher
) : RemoteData {

    // watched from main thread for toast messages
    override val remoteResponse = PublishFlow<String?>()

    private suspend fun <T : Any> checkCallAndReturn(call: Call<T>, funcName: String): T? =
        withContext(mDispatcher) {
            Timber.i("Running checkCallAndReturn()")
            var resultData: T? = null
            when (val result = api.getResult(call)) {
                is Result.Success -> {
                    remoteResponse.tryEmit("Success: remote data retrieved")
                    resultData = result.data
                }

                is Result.NetworkFailure -> remoteResponse.tryEmit(result.error.toString())
                is Result.ApiFailure -> remoteResponse.tryEmit(result.errorString)
                is Result.Loading -> Timber.d("$funcName is loading")
            }
            resultData
        }

    override suspend fun fetchTrackResponse(): List<Track>? =
        withContext(mDispatcher) {
            Timber.i("Running fetchProductResponse()")
            val call = api.soundCloudApiService.fetchTrackResponse()
            val name = object {}.javaClass.enclosingMethod?.name
            val resultData: List<TrackDto>? = checkCallAndReturn(call, name!!)
            resultData?.map { it.toDomainModel() }
        }

}

interface RemoteData {
    val remoteResponse: MutableSharedFlow<String?>
    suspend fun fetchTrackResponse(): List<Track>?
}