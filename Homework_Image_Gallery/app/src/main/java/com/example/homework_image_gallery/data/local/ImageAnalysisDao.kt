package com.example.homework_image_gallery.data.local

interface ImageAnalysisDao {
    suspend fun getById(imageId: Long): ImageAnalysisEntity?
    suspend fun upsert(entity: ImageAnalysisEntity)
}
