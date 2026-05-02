package com.example.homework_image_gallery.data.local

import android.content.Context
import java.util.concurrent.ConcurrentHashMap

class ImageAnalysisDatabase private constructor(
    private val dao: ImageAnalysisDao
) {
    fun imageAnalysisDao(): ImageAnalysisDao = dao

    companion object {
        fun create(@Suppress("UNUSED_PARAMETER") context: Context): ImageAnalysisDatabase {
            return ImageAnalysisDatabase(InMemoryImageAnalysisDao())
        }
    }
}

private class InMemoryImageAnalysisDao : ImageAnalysisDao {
    private val cache = ConcurrentHashMap<Long, ImageAnalysisEntity>()

    override suspend fun getById(imageId: Long): ImageAnalysisEntity? = cache[imageId]

    override suspend fun upsert(entity: ImageAnalysisEntity) {
        cache[entity.imageId] = entity
    }
}
