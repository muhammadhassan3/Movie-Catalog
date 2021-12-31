package com.dicoding.moviecatalog.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.moviecatalog.ui.view.main.fragment.MoviesFragment
import com.dicoding.moviecatalog.ui.view.main.fragment.TvShowFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MoviesFragment.newInstance()
            1 -> TvShowFragment.newInstance()
            else -> null as Fragment
        }
    }
}