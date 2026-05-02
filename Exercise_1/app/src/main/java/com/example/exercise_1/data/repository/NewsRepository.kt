package com.example.exercise_1.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.exercise_1.data.remote.NewsApiService
import com.example.exercise_1.data.remote.NewsPagingSource
import com.example.exercise_1.ml.SentimentAnalyzer

class NewsRepository(
    private val api: NewsApiService,
    private val sentimentAnalyzer: SentimentAnalyzer
) {
    fun getNewsPager(query: String = NewsPagingSource.DEFAULT_QUERY): Pager<Int, com.example.exercise_1.data.model.ArticleWithSentiment> {
        return Pager(
            config = PagingConfig(
                pageSize = NewsPagingSource.DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(api, sentimentAnalyzer, query) }
        )
    }
}

