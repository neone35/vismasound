package com.arturmaslov.vismasound.data.source.remote

import com.arturmaslov.vismasound.App
import com.arturmaslov.vismasound.BuildConfig
import com.arturmaslov.vismasound.data.models.AccessTokenResponse
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.models.TrackDto
import com.arturmaslov.vismasound.data.models.toDomainModel
import com.arturmaslov.vismasound.helpers.extensions.PublishFlow
import com.arturmaslov.vismasound.helpers.utils.Constants
import com.arturmaslov.vismasound.helpers.utils.SharedPreferencesManager
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

    //    Soundcloud:
//    Please be aware there is a rate limiting on amount of token you can request through
//    the Client Credentials Flow: 50 tokens in 12h per app, and 30 tokens in 1h per IP address.
//    In order to not hit the limit we highly recommend reusing one token between instances of
//    your service and implementing the Refresh Token flow to renew tokens.
    private suspend fun fetchSaveAndReturnAccessToken(): String =
        withContext(mDispatcher) {
            Timber.i("Running fetchAndSetAccessToken()")
            // get last saved refresh token and check if hour passed because of API limits
            val sharedPreferencesManager = SharedPreferencesManager(App.getAppContext())
            val existingRefreshToken = sharedPreferencesManager.retrieveLastRefreshToken()
            val hourPassed = sharedPreferencesManager.hourPassedSinceSave()
            // make a call
            val call = api.soundCloudApiService.getAccessToken(
                grantType = if (hourPassed) "client_credentials" else "refresh_token",
                clientId = BuildConfig.soundCloudClientID,
                clientSecret = BuildConfig.soundCloudClientSecret,
                // null == get new token
                refreshToken = if (hourPassed) null else existingRefreshToken
            )
            val name = object {}.javaClass.enclosingMethod?.name
            val resultData: AccessTokenResponse? = checkCallAndReturn(call, name!!)
            val accessToken = resultData?.accessToken ?: Constants.EMPTY_STRING
            val refreshToken = resultData?.refreshToken ?: Constants.EMPTY_STRING
            sharedPreferencesManager.saveSoundCloudTokenWithTime(refreshToken)
            accessToken
        }

    override suspend fun fetchRemoteTrackList(genre: String): List<Track>? =
        withContext(mDispatcher) {
            Timber.i("Running fetchRemoteTrackList()")
            val accessToken = fetchSaveAndReturnAccessToken()
            val call = api.soundCloudApiService.getTracks(
                genres = listOf(genre),
                limit = 200,
                authorization = "Bearer $accessToken"
            )
            val name = object {}.javaClass.enclosingMethod?.name
            val resultData: List<TrackDto>? = checkCallAndReturn(call, name!!)
            resultData?.map { it.toDomainModel() }
        }

}

interface RemoteData {
    val remoteResponse: MutableSharedFlow<String?>
    suspend fun fetchRemoteTrackList(genre: String): List<Track>?
}