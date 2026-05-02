package com.example.homework_image_gallery.data.analysis

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ImageAnalyzer(context: Context) {
    private val appContext = context.applicationContext
    private val imageLoader = ImageLoader(appContext)
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    suspend fun analyzeImage(imageUrl: String): List<String> = withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(appContext)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        val result = imageLoader.execute(request)
        val drawable = result.drawable ?: return@withContext emptyList()
        val bitmap = (drawable as? BitmapDrawable)?.bitmap ?: return@withContext emptyList()
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val labels = labeler.process(inputImage).await()

        labels
            .sortedByDescending { it.confidence }
            .map { it.text }
            .distinct()
            .take(3)
    }
}
