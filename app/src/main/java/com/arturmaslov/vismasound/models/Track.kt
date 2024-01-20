package com.arturmaslov.vismasound.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class TrackDto(

    @SerializedName("IMG_URL")
    val imgUrl: String? = null,

    @SerializedName("QUANTITY")
    val quantity: String? = null,

    @SerializedName("SKN")
    val skn: String? = null,

    @SerializedName("BRAND")
    val brand: String? = null,

    @SerializedName("NAME")
    val name: String? = null,

    @SerializedName("BUYER_CODE")
    val buyerCode: String? = null
)

@Entity
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val imgUrl: String? = null,
    val quantity: String? = null,
    val skn: Int? = null,
    val brand: String? = null,
    val name: String? = null,
    val buyerCode: Int? = null
)

data class Track(
    val id: Int? = null,
    val imgUrl: String? = null,
    val quantity: String? = null,
    val skn: Int? = null,
    val brand: String? = null,
    val name: String? = null,
    val buyerCode: Int? = null
)

fun TrackDto.toDomainModel(): Track {
    return Track(
        id = null,
        imgUrl = imgUrl,
        quantity = quantity,
        skn = skn?.toInt(),
        brand = brand,
        name = name,
        buyerCode = buyerCode?.toInt()
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        imgUrl = imgUrl,
        quantity = quantity,
        skn = skn,
        brand = brand,
        name = name,
        buyerCode = buyerCode
    )
}

fun TrackEntity.toDomainModel(): Track {
    return Track(
        id = id,
        imgUrl = imgUrl,
        quantity = quantity,
        skn = skn,
        brand = brand,
        name = name,
        buyerCode = buyerCode
    )
}