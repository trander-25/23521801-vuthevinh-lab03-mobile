package com.example.homework_image_gallery.ui.adapter

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.homework_image_gallery.R
import com.example.homework_image_gallery.data.model.ProcessedImage
import com.example.homework_image_gallery.databinding.ItemProcessedImageBinding

class ProcessedImageAdapter : PagingDataAdapter<ProcessedImage, ProcessedImageAdapter.ProcessedImageViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ProcessedImageViewHolder {
        val binding = ItemProcessedImageBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProcessedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProcessedImageViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class ProcessedImageViewHolder(
        private val binding: ItemProcessedImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProcessedImage) {
            binding.imageView.load(item.image.webformatURL)
            val tags = if (item.aiTags.isEmpty()) "-" else item.aiTags.joinToString(", ")
            binding.tagsText.text = binding.root.context.getString(R.string.ai_tags_label, tags)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ProcessedImage>() {
            override fun areItemsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage): Boolean {
                return oldItem.image.id == newItem.image.id
            }

            override fun areContentsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage): Boolean {
                return oldItem == newItem
            }
        }
    }
}

