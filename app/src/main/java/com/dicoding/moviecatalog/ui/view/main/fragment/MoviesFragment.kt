package com.dicoding.moviecatalog.ui.view.main.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.FilmSmallListAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.FragmentMoviesBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.ui.view.main.MainViewModel
import com.dicoding.moviecatalog.utils.*

class MoviesFragment : Fragment() {
    private var filmAdapter = FilmSmallListAdapter(
        object: FilmListInterface{
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
        val binding = FragmentMoviesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMoviesBinding.bind(view)
        binding.rvMovies.apply{
            adapter = filmAdapter
            val span = if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
            layoutManager = GridLayoutManager(requireContext(), span)
            val spaces = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
            val decoration = GridSpacingItemDecoration(span, spaces, true, 0)
            addItemDecoration(decoration)
        }

        initViewModel(binding)
    }

    private fun initViewModel(binding: FragmentMoviesBinding){
        viewModel.setMovies()
        viewModel.movies.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    showState(binding, State.SUCCESS)
                    filmAdapter.setData(it.data)
                }
                Status.ERROR -> {
                    showState(binding, State.NO_DATA)
                    val content = it.message?.getContentIfNotHandled()
                    content?.let{
                        binding.root.showSnackbar(content)
                    }
                }
                Status.LOADING -> showState(binding, State.LOADING)
                Status.NO_DATA -> showState(binding, State.NO_DATA)
            }
        }
    }

    private fun showState(binding: FragmentMoviesBinding, state: State){
        when(state){
            State.NO_DATA -> {
                binding.apply {
                    shimmerMovie.apply {
                        stopShimmer()
                        gone()
                    }
                    rvMovies.visible()
                    emptyData.root.visible()
                }
            }
            State.SUCCESS -> {
                binding.apply {
                    shimmerMovie.apply{
                        stopShimmer()
                        gone()
                    }
                    rvMovies.visible()
                    emptyData.root.gone()
                }
            }
            State.LOADING -> {
                binding.apply{
                    shimmerMovie.apply{
                        startShimmer()
                        visible()
                    }
                    rvMovies.gone()
                    emptyData.root.gone()
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