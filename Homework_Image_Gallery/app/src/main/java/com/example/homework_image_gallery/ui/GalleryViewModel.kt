package com.example.homework_image_gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.cachedIn
import com.example.homework_image_gallery.data.model.ProcessedImage
import com.example.homework_image_gallery.data.repository.GalleryRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update

class GalleryViewModel(
    repository: GalleryRepository
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val baseFlow = repository.pagerFlow().cachedIn(viewModelScope)

    @OptIn(FlowPreview::class)
    val processedImages: Flow<PagingData<ProcessedImage>> = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .combine(baseFlow) { query, pagingData ->
            if (query.isBlank()) {
                pagingData
            } else {
                pagingData.filter { processed ->
                    processed.aiTags.any { tag -> tag.contains(query, ignoreCase = true) }
                }
            }
        }

    fun updateSearchQuery(query: String) {
        searchQuery.update { query.trim() }
    }
}

