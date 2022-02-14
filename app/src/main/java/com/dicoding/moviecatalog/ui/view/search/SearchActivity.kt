package com.dicoding.moviecatalog.ui.view.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmMediumListAdapter
import com.dicoding.moviecatalog.adapter.loadstate.ItemMediumLoadStateAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.ActivitySearchBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.utils.EspressoIdlingResources
import com.dicoding.moviecatalog.utils.State
import com.dicoding.moviecatalog.utils.exception.NoDataAvailableException
import com.dicoding.moviecatalog.utils.gone
import com.dicoding.moviecatalog.utils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), FilmListInterface {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()
    private val filmAdapter = FilmMediumListAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showState(null)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            title = getString(R.string.search_title)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.setQuery(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        initViewModel()
    }

    private fun initViewModel() {
        val concatAdapter = filmAdapter.withLoadStateFooter(
            footer = ItemMediumLoadStateAdapter {
                filmAdapter.retry()
            }
        )
        var state: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        filmAdapter.addLoadStateListener {
            if(state != it.source.refresh){
                if (filmAdapter.itemCount > 0 && it.source.refresh is LoadState.NotLoading) {
                    EspressoIdlingResources.decrement()
                    showState(State.SUCCESS)
                } else if (it.source.refresh is LoadState.Loading) {
                    EspressoIdlingResources.increment()
                    showState(State.LOADING)
                } else if (it.source.refresh is LoadState.Error) {
                    EspressoIdlingResources.decrement()
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
        binding.rvList.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
        viewModel.getSearchResult().observe(this){
            filmAdapter.submitData(lifecycle, it)
        }
    }

    private fun showState(state: State?) {
        binding.apply {
            when (state) {
                State.LOADING -> {
                    shimmer.apply {
                        startShimmer()
                        visible()
                    }
                    noData.root.gone()
                    error.root.gone()
                    rvList.gone()
                }
                State.SUCCESS -> {
                    shimmer.apply {
                        stopShimmer()
                        gone()
                    }
                    noData.root.gone()
                    error.root.gone()
                    rvList.visible()
                }
                State.NO_DATA -> {
                    shimmer.apply {
                        stopShimmer()
                        gone()
                    }
                    noData.root.visible()
                    error.root.gone()
                    rvList.gone()
                }
                State.ERROR -> {
                    shimmer.apply {
                        stopShimmer()
                        gone()
                    }
                    noData.root.gone()
                    error.root.visible()
                    rvList.gone()
                }
                null -> {
                    shimmer.apply {
                        stopShimmer()
                        gone()
                    }
                    noData.root.gone()
                    error.root.gone()
                    rvList.gone()
                }
            }
        }
    }

    override fun onItemClicked(item: FilmModel) {
        val intent = Intent(this, DetailFilmActivity::class.java).apply{
            putExtra(DetailFilmActivity.MEDIA_TYPE, item.type)
            putExtra(DetailFilmActivity.ID, item.id)
        }
        startActivity(intent)
    }
}