package com.dicoding.moviecatalog.ui.view.main.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmSmallPagingAdapter
import com.dicoding.moviecatalog.adapter.loadstate.ItemSmallLoadStateAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.FragmentTvShowBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.ui.view.main.MainViewModel
import com.dicoding.moviecatalog.utils.*
import com.dicoding.moviecatalog.utils.exception.NoDataAvailableException
import kotlinx.coroutines.launch

class TvShowFragment : Fragment() {
    private var filmAdapter = FilmSmallPagingAdapter(
        object : FilmListInterface {
            override fun onItemClicked(item: FilmModel) {
                val intent = Intent(requireContext(), DetailFilmActivity::class.java)
                intent.putExtra(DetailFilmActivity.ID, item.id)
                intent.putExtra(DetailFilmActivity.MEDIA_TYPE, "tv")
                startActivity(intent)
            }
        }
    )
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTvShowBinding.bind(view)

        binding.apply{
            val concatAdapter = filmAdapter.withLoadStateFooter(
                footer = ItemSmallLoadStateAdapter{
                    filmAdapter.retry()
                }
            )
            var state: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
            filmAdapter.addLoadStateListener {
                if(state != it.source.refresh) {
                    if (filmAdapter.itemCount > 0 && it.source.refresh is LoadState.NotLoading) {
                        showState(binding, State.SUCCESS)
                    } else if (it.source.refresh is LoadState.Loading) {
                        showState(binding, State.LOADING)
                    } else if (it.source.refresh is LoadState.Error) {
                        val errorType = (it.refresh as LoadState.Error).error
                        if (errorType is NoDataAvailableException) {
                            showState(binding, State.NO_DATA)
                        } else showState(binding, State.ERROR)
                    }
                    state = it.source.refresh
                }
            }
            rvTvShow.apply {
                adapter = concatAdapter
                val span =
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
                layoutManager = GridLayoutManager(requireContext(), span)
                val spaces = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
                val decoration = GridSpacingItemDecoration(span, spaces, true, 0)
                addItemDecoration(decoration)
            }
        }
        initViewModel(binding)
    }

    private fun initViewModel(binding: FragmentTvShowBinding) {
        lifecycleScope.launch{
            viewModel.getSeries().collect{
                filmAdapter.submitData(it)
            }
        }
    }

    private fun showState(binding: FragmentTvShowBinding, state: State) {
        when (state) {
            State.LOADING -> {
                binding.apply {
                    shimmerSeries.apply {
                        visible()
                        startShimmer()
                    }
                    rvTvShow.gone()
                    emptyData.root.gone()
                    error.root.gone()
                }
            }
            State.NO_DATA -> {
                binding.apply {
                    shimmerSeries.apply {
                        stopShimmer()
                        gone()
                    }
                    rvTvShow.gone()
                    emptyData.root.visible()
                    error.root.gone()
                }
            }
            State.SUCCESS -> {
                binding.apply {
                    shimmerSeries.apply {
                        stopShimmer()
                        gone()
                    }
                    rvTvShow.visible()
                    emptyData.root.gone()
                    error.root.gone()
                }
            }
            State.ERROR -> binding.apply {
                shimmerSeries.apply {
                    stopShimmer()
                    gone()
                }
                rvTvShow.gone()
                emptyData.root.gone()

                error.root.visible()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TvShowFragment()
    }
}