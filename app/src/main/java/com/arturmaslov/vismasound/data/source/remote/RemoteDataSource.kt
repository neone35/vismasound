package com.arturmaslov.vismasound.data.source.remote

import com.arturmaslov.vismasound.BuildConfig
import com.arturmaslov.vismasound.data.models.AccessTokenResponse
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.models.TrackDto
import com.arturmaslov.vismasound.data.models.toDomainModel
import com.arturmaslov.vismasound.helpers.cache.TokenTimeCacheManager
import com.arturmaslov.vismasound.helpers.extensions.PublishFlow
import com.arturmaslov.vismasound.helpers.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import retrofit2.Call
import timber.log.Timber

class RemoteDataSource(
    private val api: Api,
    private val mDispatcher: CoroutineDispatcher,
    private val tokenTimeCacheManager: TokenTimeCacheManager
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

    //    Soundcloud:
//    Please be aware there is a rate limiting on amount of token you can request through
//    the Client Credentials Flow: 50 tokens in 12h per app, and 30 tokens in 1h per IP address.
//    In order to not hit the limit we highly recommend reusing one token between instances of
//    your service and implementing the Refresh Token flow to renew tokens.
//    Currently a token lives around 1 hour.
    private suspend fun fetchSaveAndReturnAccessToken(): String =
        withContext(mDispatcher) {
            val name = object {}.javaClass.enclosingMethod?.name
            Timber.i("Running $name")

            // get last saved refresh token and check if hour passed because of API limits
            val existingRefreshToken = tokenTimeCacheManager.retrieveLastRefreshToken()
            val timePassed = tokenTimeCacheManager.checkIfHourPassedSinceSave()
            // make a call
            val call = api.soundCloudApiService.getAccessToken(
                grantType = if (timePassed) CLIENT_CREDENTIALS else REFRESH_TOKEN,
                clientId = BuildConfig.soundCloudClientID,
                clientSecret = BuildConfig.soundCloudClientSecret,
                // null == get new token
                refreshToken = if (timePassed) null else existingRefreshToken
            )
            val resultData: AccessTokenResponse? = checkCallAndReturn(call, name!!)
            val accessToken = resultData?.accessToken ?: Constants.EMPTY_STRING
            val refreshToken = resultData?.refreshToken ?: Constants.EMPTY_STRING
            tokenTimeCacheManager.saveSoundCloudTokenWithTime(refreshToken)
            accessToken
        }

    override suspend fun fetchRemoteTrackList(
        genre: String?,
        amount: Int?
    ): List<Track>? =
        withContext(mDispatcher) {
            val name = object {}.javaClass.enclosingMethod?.name
            Timber.i("Running $name")

            val accessToken = fetchSaveAndReturnAccessToken()
            val call = api.soundCloudApiService.getTracks(
                genres = genre?.let { listOf(genre) },
                limit = amount ?: SOUNDCLOUD_REQ_LIMIT,
                authorization = "Bearer $accessToken"
            )
            val resultData: List<TrackDto>? = checkCallAndReturn(call, name!!)
            resultData?.map { it.toDomainModel() }
        }

    companion object {
        const val SOUNDCLOUD_REQ_LIMIT = 200
        const val CLIENT_CREDENTIALS = "client_credentials"
        const val REFRESH_TOKEN = "refresh_token"
    }
}

interface RemoteData {
    val remoteResponse: MutableSharedFlow<String?>
    suspend fun fetchRemoteTrackList(
        genre: String?,
        amount: Int? = null
    ): List<Track>?
}