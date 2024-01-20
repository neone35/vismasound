package com.arturmaslov.vismasound.source.remote

import com.arturmaslov.vismasound.models.TrackDto
import retrofit2.Call
import retrofit2.http.GET

interface SoundCloudApiService {

    @GET("query_results-2023-10-24_20438.json")
    fun fetchTrackResponse(): Call<List<TrackDto>>

}