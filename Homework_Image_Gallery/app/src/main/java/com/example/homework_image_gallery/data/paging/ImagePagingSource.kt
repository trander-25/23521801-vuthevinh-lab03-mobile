package com.example.homework_image_gallery.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.homework_image_gallery.BuildConfig
import com.example.homework_image_gallery.data.analysis.ImageAnalyzer
import com.example.homework_image_gallery.data.local.ImageAnalysisDao
import com.example.homework_image_gallery.data.local.ImageAnalysisEntity
import com.example.homework_image_gallery.data.model.ProcessedImage
import com.example.homework_image_gallery.data.remote.ImageItem
import com.example.homework_image_gallery.data.remote.PixabayApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PixabayPagingSource(
    private val api: PixabayApi,
    private val analyzer: ImageAnalyzer,
    private val analysisDao: ImageAnalysisDao,
    private val query: String,
    private val perPage: Int
) : PagingSource<Int, ProcessedImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProcessedImage> {
        val page = params.key ?: 1
        return try {
            val response = api.searchImages(
                apiKey = BuildConfig.PIXABAY_API_KEY,
                query = query,
                page = page,
                perPage = perPage
            )

            val processed = coroutineScope {
                response.hits.map { item ->
                    async { ProcessedImage(image = item, aiTags = getTagsForImage(item)) }
                }.awaitAll()
            }

            LoadResult.Page(
                data = processed,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.hits.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProcessedImage>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    private suspend fun getTagsForImage(item: ImageItem): List<String> {
        val cached = analysisDao.getById(item.id)
        if (cached != null) return cached.tags

        val tags = analyzer.analyzeImage(item.webformatURL)
        analysisDao.upsert(ImageAnalysisEntity(imageId = item.id, tags = tags))
        return tags
    }
}
