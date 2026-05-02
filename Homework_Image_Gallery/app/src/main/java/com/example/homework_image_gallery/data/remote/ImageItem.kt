package com.example.homework_image_gallery.data.remote

import com.google.gson.annotations.SerializedName

data class ImageItem(
    @SerializedName("id") val id: Long,
    @SerializedName("webformatURL") val webformatURL: String,
    @SerializedName("largeImageURL") val largeImageURL: String?,
    @SerializedName("tags") val tags: String?
)

