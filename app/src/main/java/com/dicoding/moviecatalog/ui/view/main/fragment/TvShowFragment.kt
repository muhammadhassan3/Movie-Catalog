package com.dicoding.moviecatalog.ui.view.main.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmSmallListAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.FragmentTvShowBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.ui.view.main.MainViewModel
import com.dicoding.moviecatalog.utils.*

class TvShowFragment : Fragment() {
    private var filmAdapter = FilmSmallListAdapter(
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

        binding.rvTvShow.apply {
            adapter = filmAdapter
            val span =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
            layoutManager = GridLayoutManager(requireContext(), span)
            val spaces = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
            val decoration = GridSpacingItemDecoration(span, spaces, true, 0)
            addItemDecoration(decoration)
        }

        initViewModel(binding)
    }

    private fun initViewModel(binding: FragmentTvShowBinding) {
        viewModel.setSeries()
        viewModel.series.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.NO_DATA -> showState(binding, State.NO_DATA)
                Status.LOADING -> showState(binding, State.LOADING)
                Status.ERROR -> {
                    showState(binding, State.NO_DATA)
                    val content = it.message?.getContentIfNotHandled()
                    content?.let { message ->
                        binding.root.showSnackbar(message)
                    }

                }
                Status.SUCCESS -> {
                    showState(binding, State.SUCCESS)
                    filmAdapter.setData(it.data)
                }
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
                    rvTvShow.visible()
                    emptyData.root.gone()
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
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TvShowFragment()
    }
}