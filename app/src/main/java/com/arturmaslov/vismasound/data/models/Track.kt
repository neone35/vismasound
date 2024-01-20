package com.arturmaslov.vismasound.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class TrackDto(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("artwork_url")
    val imgUrl: String? = null,

    @SerializedName("stream_url")
    val streamUrl: String? = null,

    @SerializedName("download_url")
    val downloadUrl: String? = null,

    @SerializedName("waveform_url")
    val waveformUrl: String? = null,

    @SerializedName("duration")
    val duration: String? = null,

    @SerializedName("playback_count")
    val playbackCount: String? = null,

    @SerializedName("favoritings_count")
    val favoritingsCount: String? = null,

    )

@Entity
data class TrackEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val title: String? = null,
    val createdAt: String? = null,
    val imgUrl: String? = null,
    val streamUrl: String? = null,
    val downloadUrl: String? = null,
    val waveformUrl: String? = null,
    val duration: String? = null,
    val playbackCount: String? = null,
    val favoritingsCount: String? = null,
)

data class Track(
    val id: Int? = null,
    val title: String? = null,
    val createdAt: String? = null,
    val imgUrl: String? = null,
    val streamUrl: String? = null,
    val downloadUrl: String? = null,
    val waveformUrl: String? = null,
    val duration: String? = null,
    val playbackCount: String? = null,
    val favoritingsCount: String? = null,
)

fun TrackDto.toDomainModel(): Track {
    return Track(
        id = id,
        title = title,
        createdAt = createdAt,
        imgUrl = imgUrl,
        streamUrl = streamUrl,
        downloadUrl = downloadUrl,
        waveformUrl = waveformUrl,
        duration = duration,
        playbackCount = playbackCount,
        favoritingsCount = favoritingsCount
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        title = title,
        createdAt = createdAt,
        imgUrl = imgUrl,
        streamUrl = streamUrl,
        downloadUrl = downloadUrl,
        waveformUrl = waveformUrl,
        duration = duration,
        playbackCount = playbackCount,
        favoritingsCount = favoritingsCount
    )
}

fun TrackEntity.toDomainModel(): Track {
    return Track(
        id = id,
        title = title,
        createdAt = createdAt,
        imgUrl = imgUrl,
        streamUrl = streamUrl,
        downloadUrl = downloadUrl,
        waveformUrl = waveformUrl,
        duration = duration,
        playbackCount = playbackCount,
        favoritingsCount = favoritingsCount
    )
}