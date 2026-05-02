package com.example.exercise_1.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class NewsTabsAdapter(
    activity: FragmentActivity,
    private val tabs: List<NewsTab>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        return NewsListFragment.newInstance(tabs[position].query)
    }

    data class NewsTab(val title: String, val query: String)
}

