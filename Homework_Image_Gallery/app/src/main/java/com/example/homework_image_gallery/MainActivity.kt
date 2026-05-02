package com.example.homework_image_gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework_image_gallery.data.analysis.ImageAnalyzer
import com.example.homework_image_gallery.data.local.ImageAnalysisDatabase
import com.example.homework_image_gallery.data.remote.ApiClient
import com.example.homework_image_gallery.data.repository.GalleryRepository
import com.example.homework_image_gallery.databinding.ActivityMainBinding
import com.example.homework_image_gallery.ui.GalleryViewModel
import com.example.homework_image_gallery.ui.GalleryViewModelFactory
import com.example.homework_image_gallery.ui.adapter.ImageLoadStateAdapter
import com.example.homework_image_gallery.ui.adapter.ProcessedImageAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: GalleryViewModel by viewModels {
        val database = ImageAnalysisDatabase.create(applicationContext)
        val repository = GalleryRepository(
            api = ApiClient.create(),
            analyzer = ImageAnalyzer(applicationContext),
            analysisDao = database.imageAnalysisDao()
        )
        GalleryViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ProcessedImageAdapter()
        val loadStateAdapter = ImageLoadStateAdapter { adapter.retry() }

        binding.imageRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.imageRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = loadStateAdapter,
            footer = loadStateAdapter
        )

        binding.retryButton.setOnClickListener { adapter.retry() }
        binding.searchInput.doAfterTextChanged { text ->
            viewModel.updateSearchQuery(text?.toString().orEmpty())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.processedImages.collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }
                launch {
                    adapter.loadStateFlow.collectLatest { loadState ->
                        val isLoading = loadState.refresh is androidx.paging.LoadState.Loading
                        val isError = loadState.refresh is androidx.paging.LoadState.Error
                        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
                        binding.errorLayout.visibility = if (isError) android.view.View.VISIBLE else android.view.View.GONE
                    }
                }
            }
        }
    }
}