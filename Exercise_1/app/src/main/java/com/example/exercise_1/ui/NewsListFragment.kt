package com.example.exercise_1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercise_1.R
import com.example.exercise_1.data.remote.NewsApiService
import com.example.exercise_1.data.repository.NewsRepository
import com.example.exercise_1.ml.SentimentAnalyzer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsListFragment : Fragment() {
    private lateinit var sentimentAnalyzer: SentimentAnalyzer
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val query = requireArguments().getString(ARG_QUERY).orEmpty()
        sentimentAnalyzer = SentimentAnalyzer(requireContext().applicationContext)
        val repository = NewsRepository(NewsApiService.create(), sentimentAnalyzer)
        val factory = NewsViewModelFactory(repository, query)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.newsRecyclerView)
        val adapter = NewsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagedArticles.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sentimentAnalyzer.close()
    }

    companion object {
        private const val ARG_QUERY = "arg_query"

        fun newInstance(query: String): NewsListFragment {
            return NewsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
        }
    }
}
