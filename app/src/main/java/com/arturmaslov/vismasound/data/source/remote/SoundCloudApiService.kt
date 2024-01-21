package com.arturmaslov.vismasound.data.source.remote

import com.arturmaslov.vismasound.data.models.AccessTokenResponse
import com.arturmaslov.vismasound.data.models.TrackDto
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SoundCloudApiService {
    @FormUrlEncoded
    @POST("oauth2/token")
    fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String?,
    ): Call<AccessTokenResponse>

    @GET("tracks")
    fun getTracks(
        @Query("genres") genres: List<String>?,
        @Query("limit") limit: Int,
        @Header("Authorization") authorization: String
    ): Call<List<TrackDto>>

}