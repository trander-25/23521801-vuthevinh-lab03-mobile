package com.example.exercise_1.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.exercise_1.data.model.ArticleWithSentiment
import com.example.exercise_1.ml.SentimentAnalyzer

class NewsPagingSource(
    private val api: NewsApiService,
    private val sentimentAnalyzer: SentimentAnalyzer,
    private val query: String = DEFAULT_QUERY,
    private val pageSize: Int = DEFAULT_PAGE_SIZE
) : PagingSource<Int, ArticleWithSentiment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleWithSentiment> {
        return try {
            val page = params.key ?: 1
            val response = api.getEverything(
                query = query,
                page = page,
                pageSize = pageSize
            )

            val items = response.articles.orEmpty().map { article ->
                val content = article.content?.takeIf { it.isNotBlank() }
                    ?: article.title.orEmpty()
                val scores = sentimentAnalyzer.analyze(content)
                ArticleWithSentiment(article, scores.positive, scores.negative)
            }

            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleWithSentiment>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition)
        return anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }

    companion object {
        const val DEFAULT_QUERY = "android"
        const val DEFAULT_PAGE_SIZE = 20
    }
}

