package com.example.homework_image_gallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.homework_image_gallery.data.analysis.ImageAnalyzer
import com.example.homework_image_gallery.data.local.ImageAnalysisDao
import com.example.homework_image_gallery.data.model.ProcessedImage
import com.example.homework_image_gallery.data.paging.PixabayPagingSource
import com.example.homework_image_gallery.data.remote.PixabayApi
import kotlinx.coroutines.flow.Flow

class GalleryRepository(
    private val api: PixabayApi,
    private val analyzer: ImageAnalyzer,
    private val analysisDao: ImageAnalysisDao
) {
    private val perPage = 20
    private val defaultQuery = "nature"

    fun pagerFlow(): Flow<PagingData<ProcessedImage>> {
        return Pager(
            config = PagingConfig(
                pageSize = perPage,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PixabayPagingSource(
                    api = api,
                    analyzer = analyzer,
                    analysisDao = analysisDao,
                    query = defaultQuery,
                    perPage = perPage
                )
            }
        ).flow
    }
}
