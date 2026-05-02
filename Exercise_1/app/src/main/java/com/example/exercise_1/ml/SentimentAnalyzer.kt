package com.example.exercise_1.ml

import android.content.Context
import android.util.Log
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier

class SentimentAnalyzer(context: Context) {
    private val classifier: NLClassifier? = try {
        NLClassifier.createFromFile(context, MODEL_FILE)
    } catch (exception: Exception) {
        Log.w(TAG, "Sentiment model not available; defaulting scores to 0", exception)
        null
    }

    fun analyze(text: String): SentimentResult {
        val trimmed = text.trim()
        val localClassifier = classifier ?: return SentimentResult(positive = 0f, negative = 0f)
        if (trimmed.isBlank()) {
            return SentimentResult(positive = 0f, negative = 0f)
        }

        val results = localClassifier.classify(trimmed)
        var positive = 0f
        var negative = 0f

        for (category in results) {
            when (category.label.lowercase()) {
                "positive" -> positive = category.score
                "negative" -> negative = category.score
            }
        }

        return SentimentResult(positive, negative)
    }

    fun close() {
        classifier?.close()
    }

    data class SentimentResult(val positive: Float, val negative: Float)

    companion object {
        private const val MODEL_FILE = "sentiment_model.tflite"
        private const val TAG = "SentimentAnalyzer"
    }
}
