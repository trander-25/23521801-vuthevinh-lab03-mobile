package com.example.exercise_1.data.model

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?
)

data class NewsResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<Article>?
)

data class ArticleWithSentiment(
    val article: Article,
    val positive: Float,
    val negative: Float
)
