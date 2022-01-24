package com.dicoding.moviecatalog.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmSmallListAdapter
import com.dicoding.moviecatalog.adapter.SectionPagerAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.ActivityMainBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private var animationDuration = 0
    private var randomAdapter = FilmSmallListAdapter(object : FilmListInterface {
        override fun onItemClicked(item: FilmModel) {
            val intent = Intent(this@MainActivity, DetailFilmActivity::class.java)
            intent.putExtra(DetailFilmActivity.ID, item.id)
            intent.putExtra(DetailFilmActivity.MEDIA_TYPE, item.type)
            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        initTabLayout()
        initListRandom()
    }

    private fun initListRandom() {
        binding.layoutCollapsingToolbar.rvRandom.apply {
            adapter = randomAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

        }
        viewModel.setRandom(RANDOM_VALUE)
        viewModel.random.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    showState(State.SUCCESS)
                    randomAdapter.setData(it.data)
                }
                Status.LOADING -> {
                    showState(State.LOADING)
                }
                Status.ERROR -> {
                    showState(State.NO_DATA)
                    val content = it.message?.getContentIfNotHandled()
                    content?.let { message ->
                        binding.root.showSnackbar(message)
                    }
                }
                Status.NO_DATA -> {
                    showState(State.NO_DATA)
                }
            }
        })
    }

    private fun initTabLayout() {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        binding.layoutCollapsingToolbar.shimmerRandom.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.layoutCollapsingToolbar.shimmerRandom.stopShimmer()
    }

    private fun showState(state: State) {
        when (state) {
            State.LOADING -> {
                binding.layoutCollapsingToolbar.apply {
                    shimmerRandom.apply {
                        startShimmer()
                        visible()
                    }
                    rvRandom.visible()
                    emptyData.root.gone()
                }
            }
            State.SUCCESS -> {
                binding.layoutCollapsingToolbar.apply {
                    rvRandom.visible()
                    shimmerRandom.apply {
                        stopShimmer()
                        gone()
                    }
                    emptyData.root.gone()
                }
            }
            State.NO_DATA -> {
                binding.layoutCollapsingToolbar.apply {
                    emptyData.root.visible()
                    shimmerRandom.apply {
                        stopShimmer()
                        gone()
                    }
                    rvRandom.gone()
                }
            }
        }
    }

    companion object {
        private const val RANDOM_VALUE = 12

        @StringRes
        private val TAB_TITLE = arrayOf(
            R.string.movies,
            R.string.tv_show
        )
    }
}