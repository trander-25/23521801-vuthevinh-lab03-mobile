package com.example.exercise_1.ui

import android.content.res.ColorStateList
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.exercise_1.R
import com.example.exercise_1.data.model.ArticleWithSentiment
import com.google.android.material.card.MaterialCardView
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class NewsAdapter : PagingDataAdapter<ArticleWithSentiment, NewsAdapter.NewsViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView = itemView.findViewById<MaterialCardView>(R.id.newsCard)
        private val sentimentStrip = itemView.findViewById<View>(R.id.sentimentStrip)
        private val sentimentBadge = itemView.findViewById<TextView>(R.id.newsSentiment)
        private val imageView = itemView.findViewById<ImageView>(R.id.newsImage)
        private val titleView = itemView.findViewById<TextView>(R.id.newsTitle)
        private val descriptionView = itemView.findViewById<TextView>(R.id.newsDescription)
        private val dateView = itemView.findViewById<TextView>(R.id.newsDate)

        fun bind(item: ArticleWithSentiment) {
            val article = item.article
            val context = itemView.context
            val cleanedTitle = cleanHtml(article.title).orEmpty()
            val cleanedContent = cleanContent(article.content) ?: cleanedTitle
            titleView.text = cleanedTitle
            descriptionView.text = cleanedContent
            dateView.text = formatDate(article.publishedAt)

            imageView.load(article.urlToImage) {
                crossfade(true)
                error(R.drawable.ic_launcher_foreground)
                placeholder(R.drawable.ic_launcher_foreground)
            }

            val isPositive = item.positive > item.negative
            val isNegative = item.negative > item.positive
            val sentimentColorRes = when {
                isPositive -> R.color.sentiment_positive
                isNegative -> R.color.sentiment_negative
                else -> R.color.sentiment_neutral
            }
            val sentimentTextRes = when {
                isPositive -> R.string.sentiment_positive
                isNegative -> R.string.sentiment_negative
                else -> R.string.sentiment_neutral
            }
            val sentimentColor = ContextCompat.getColor(context, sentimentColorRes)

            cardView.strokeColor = sentimentColor
            sentimentStrip.setBackgroundColor(sentimentColor)
            sentimentBadge.text = context.getString(sentimentTextRes)
            sentimentBadge.backgroundTintList = ColorStateList.valueOf(sentimentColor)
        }

        private fun cleanHtml(text: String?): String? {
            val raw = text?.trim().orEmpty()
            if (raw.isBlank()) {
                return null
            }
            val parsed = Html.fromHtml(raw, Html.FROM_HTML_MODE_LEGACY).toString()
            return parsed.replace(Regex("\\s+"), " ").trim()
        }

        private fun cleanContent(content: String?): String? {
            val trimmed = content?.trim().orEmpty()
            if (trimmed.isBlank()) {
                return null
            }
            return trimmed.replace(Regex("\\s*\\[\\+\\d+ chars\\]"), "").trim()
        }

        private fun formatDate(rawDate: String?): String {
            val fallback = rawDate.orEmpty()
            if (fallback.isBlank()) {
                return ""
            }
            return try {
                val parsed = OffsetDateTime.parse(fallback)
                parsed.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            } catch (exception: DateTimeParseException) {
                fallback
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ArticleWithSentiment>() {
            override fun areItemsTheSame(
                oldItem: ArticleWithSentiment,
                newItem: ArticleWithSentiment
            ): Boolean {
                return oldItem.article.title == newItem.article.title &&
                    oldItem.article.publishedAt == newItem.article.publishedAt
            }

            override fun areContentsTheSame(
                oldItem: ArticleWithSentiment,
                newItem: ArticleWithSentiment
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
