package com.example.exercise_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.exercise_1.ui.TabsConfig
import com.example.exercise_1.ui.NewsTabsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.newsTabs)
        val viewPager = findViewById<ViewPager2>(R.id.newsPager)
        val tabs = TabsConfig.tabs
        viewPager.adapter = NewsTabsAdapter(this, tabs)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position].title
        }.attach()
    }
}