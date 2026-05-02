package com.example.homework_image_gallery.data.model

import com.example.homework_image_gallery.data.remote.ImageItem

data class ProcessedImage(
    val image: ImageItem,
    val aiTags: List<String>
)

