package com.dicoding.moviecatalog.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.moviecatalog.ui.view.favorite.fragment.FavoriteMoviesFragment
import com.dicoding.moviecatalog.ui.view.favorite.fragment.FavoriteSeriesFragment

class FavoriteSectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FavoriteMoviesFragment.newInstance()
            1 -> FavoriteSeriesFragment.newInstance()
            else -> null as Fragment
        }
    }
}