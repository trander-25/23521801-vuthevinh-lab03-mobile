package com.example.exercise_1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.exercise_1.data.model.ArticleWithSentiment
import com.example.exercise_1.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsViewModel(
    repository: NewsRepository,
    query: String
) : ViewModel() {
    val pagedArticles: Flow<PagingData<ArticleWithSentiment>> =
        repository.getNewsPager(query).flow.cachedIn(viewModelScope)
}

class NewsViewModelFactory(
    private val repository: NewsRepository,
    private val query: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(repository, query) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
