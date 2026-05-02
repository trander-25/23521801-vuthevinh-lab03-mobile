package com.example.homework_image_gallery.data.local

object TagsConverter {
    fun fromTags(tags: List<String>): String = tags.joinToString("|")

    fun toTags(stored: String): List<String> =
        if (stored.isBlank()) emptyList() else stored.split("|").map { it.trim() }.filter { it.isNotEmpty() }
}
