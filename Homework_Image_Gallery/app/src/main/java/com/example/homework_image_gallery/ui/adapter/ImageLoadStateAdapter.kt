package com.example.homework_image_gallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_image_gallery.databinding.ItemLoadStateBinding

class ImageLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ImageLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: ItemLoadStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.loadStateRetryButton.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            val isLoading = loadState is LoadState.Loading
            val isError = loadState is LoadState.Error

            binding.loadStateProgress.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
            binding.loadStateErrorText.visibility = if (isError) android.view.View.VISIBLE else android.view.View.GONE
            binding.loadStateRetryButton.visibility = if (isError) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}

