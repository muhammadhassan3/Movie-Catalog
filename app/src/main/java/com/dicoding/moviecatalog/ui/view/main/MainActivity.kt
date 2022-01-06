package com.dicoding.moviecatalog.ui.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmSmallListAdapter
import com.dicoding.moviecatalog.adapter.SectionPagerAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.ActivityMainBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var randomAdapter = FilmSmallListAdapter(object: FilmListInterface{
        override fun onItemClicked(item: FilmModel) {
            val intent = Intent(this@MainActivity,DetailFilmActivity::class.java)
            intent.putExtra(DetailFilmActivity.FILM, item)
            startActivity(intent)
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabLayout()
        initListRandom()
    }

    private fun initListRandom(){
        binding.layoutCollapsingToolbar.rvRandom.apply{
            adapter = randomAdapter
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)

        }
        viewModel.setRandom(RANDOM_VALUE)
        viewModel.random.observe(this,{
            randomAdapter.setData(it)
        })
    }

    private fun initTabLayout(){
        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }

    companion object{
        private const val RANDOM_VALUE = 12
        @StringRes
        private val TAB_TITLE = arrayOf(
            R.string.movies,
            R.string.tv_show
        )
    }
}