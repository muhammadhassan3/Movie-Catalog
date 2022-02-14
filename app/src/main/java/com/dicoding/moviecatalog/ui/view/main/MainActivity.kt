package com.dicoding.moviecatalog.ui.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmSmallPagingAdapter
import com.dicoding.moviecatalog.adapter.SectionPagerAdapter
import com.dicoding.moviecatalog.adapter.loadstate.ItemSmallLoadStateAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.ActivityMainBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.ui.view.favorite.FavoriteActivity
import com.dicoding.moviecatalog.ui.view.search.SearchActivity
import com.dicoding.moviecatalog.utils.*
import com.dicoding.moviecatalog.utils.exception.NoDataAvailableException
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private var randomAdapter = FilmSmallPagingAdapter(object : FilmListInterface {
        override fun onItemClicked(item: FilmModel) {
            val intent = Intent(this@MainActivity, DetailFilmActivity::class.java)
            intent.putExtra(DetailFilmActivity.ID, item.id)
            intent.putExtra(DetailFilmActivity.MEDIA_TYPE, item.type)
            startActivity(intent)
        }
    })
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initTabLayout()
        initListRandom()

        binding.apply{
            setSupportActionBar(toolbar)
            supportActionBar?.apply{
                title = ""
            }
            btnBookmark.setOnClickListener {
                startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            }
            layoutCollapsingToolbar.error.btnRetry.setOnClickListener {
                randomAdapter.retry()
            }
            appbar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener{
                var isShow = false
                var scrollRange = -1
                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    appBarLayout?.let {
                        if (scrollRange == -1) {
                            scrollRange = it.totalScrollRange
                        }
                        if(scrollRange + verticalOffset == 0){
                            isShow = true
                            showOption(R.id.action_search)
                        }else if(isShow){
                            isShow = false
                            hideOption(R.id.action_search)
                        }
                    }
                }
            })
            btnSearch.setOnClickListener{
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
    }

    private fun showOption(id: Int){
        val item = menu.findItem(id)
        item.isVisible = true
    }

    private fun hideOption(id: Int){
        val item = menu.findItem(id)
        item.isVisible = false
    }

    private fun initListRandom() {
        val concatAdapter = randomAdapter.withLoadStateFooter(
            footer = ItemSmallLoadStateAdapter{
                randomAdapter.retry()
            }
        )
        var state: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        randomAdapter.addLoadStateListener {
            if(state != it.source.refresh) {
                if (randomAdapter.itemCount > 0 && it.source.refresh is LoadState.NotLoading) {
                    showState(State.SUCCESS)
                } else if (it.source.refresh is LoadState.Loading) {
                    showState(State.LOADING)
                } else if (it.source.refresh is LoadState.Error) {
                    val errorType = (it.refresh as LoadState.Error).error
                    if (errorType is NoDataAvailableException) {
                        showState(State.NO_DATA)
                    } else {
                        showState(State.ERROR)
                    }
                }
                state = it.source.refresh
            }
        }
        binding.layoutCollapsingToolbar.rvRandom.apply {
            adapter = concatAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        lifecycleScope.launch {
            viewModel.getTrending().collect {
                randomAdapter.submitData(it)
            }
        }
    }

    private fun initTabLayout() {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let{
                    supportActionBar?.title = resources.getString(R.string.popular_title, it.text)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        binding.layoutCollapsingToolbar.shimmerRandom.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.layoutCollapsingToolbar.shimmerRandom.stopShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.search, menu)
        hideOption(R.id.action_search)
        return true
    }

    private fun showState(state: State) {
        when (state) {
            State.LOADING -> {
                binding.layoutCollapsingToolbar.apply {
                    shimmerRandom.apply {
                        startShimmer()
                        visible()
                    }
                    rvRandom.gone()
                    emptyData.root.gone()
                    error.root.gone()
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
                    error.root.gone()
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
                    error.root.gone()
                }
            }
            State.ERROR -> {
                binding.layoutCollapsingToolbar.apply {
                    emptyData.root.gone()
                    shimmerRandom.apply {
                        stopShimmer()
                        gone()
                    }
                    rvRandom.gone()

                    error.root.visible()
                }
            }
        }
    }

    companion object {

        @StringRes
        private val TAB_TITLE = arrayOf(
            R.string.movies,
            R.string.tv_show
        )
    }
}