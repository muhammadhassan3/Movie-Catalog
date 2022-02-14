package com.dicoding.moviecatalog.ui.view.main.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmSmallPagingAdapter
import com.dicoding.moviecatalog.adapter.loadstate.ItemSmallLoadStateAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.FragmentMoviesBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.ui.view.main.MainViewModel
import com.dicoding.moviecatalog.utils.GridSpacingItemDecoration
import com.dicoding.moviecatalog.utils.State
import com.dicoding.moviecatalog.utils.exception.NoDataAvailableException
import com.dicoding.moviecatalog.utils.gone
import com.dicoding.moviecatalog.utils.visible
import kotlinx.coroutines.launch

class MoviesFragment : Fragment() {
    private var filmAdapter = FilmSmallPagingAdapter(
        object : FilmListInterface {
            override fun onItemClicked(item: FilmModel) {
                val intent = Intent(requireContext(), DetailFilmActivity::class.java)
                intent.putExtra(DetailFilmActivity.ID, item.id)
                intent.putExtra(DetailFilmActivity.MEDIA_TYPE, "movie")
                startActivity(intent)
            }
        }
    )
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMoviesBinding.bind(view)
        binding.apply {
            val concatAdapter = filmAdapter.withLoadStateFooter(
                footer = ItemSmallLoadStateAdapter {
                    filmAdapter.retry()
                }
            )
            var state: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
            filmAdapter.addLoadStateListener {
                if (state != it.source.refresh) {
                    if (filmAdapter.itemCount > 0 && it.source.refresh is LoadState.NotLoading) {
                        showState(State.SUCCESS, binding)
                    } else if (it.source.refresh is LoadState.Loading) {
                        showState(State.LOADING, binding)
                    } else if (it.source.refresh is LoadState.Error) {
                        val errorType = (it.refresh as LoadState.Error).error
                        if (errorType is NoDataAvailableException) {
                            showState(State.NO_DATA, binding)
                        } else showState(State.ERROR, binding)
                    }
                    state = it.source.refresh
                }
            }
            rvMovies.apply {
                adapter = concatAdapter
                val span =
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
                layoutManager = GridLayoutManager(requireContext(), span)
                val spaces = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
                val decoration = GridSpacingItemDecoration(span, spaces, true, 0)
                addItemDecoration(decoration)
            }
            error.btnRetry.setOnClickListener {
                filmAdapter.retry()
            }

            initViewModel()
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.getMovies().collect{
                filmAdapter.submitData(it)
            }
        }
    }

    private fun showState(state: State, binding: FragmentMoviesBinding) {
        when (state) {
            State.NO_DATA -> {
                binding.apply {
                    shimmerMovie.apply {
                        stopShimmer()
                        gone()
                    }
                    rvMovies.gone()
                    emptyData.root.visible()
                    error.root.gone()
                }
            }
            State.SUCCESS -> {
                binding.apply {
                    shimmerMovie.apply {
                        stopShimmer()
                        gone()
                    }
                    rvMovies.visible()
                    emptyData.root.gone()
                    error.root.gone()
                }
            }
            State.LOADING -> {
                binding.apply {
                    shimmerMovie.apply {
                        startShimmer()
                        visible()
                    }
                    rvMovies.gone()
                    emptyData.root.gone()
                    error.root.gone()
                }
            }
            State.ERROR -> {
                binding.apply {
                    shimmerMovie.apply {
                        stopShimmer()
                        gone()
                    }
                    rvMovies.gone()
                    emptyData.root.gone()
                    error.root.visible()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MoviesFragment()
    }
}