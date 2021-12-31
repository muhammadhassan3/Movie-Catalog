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
import com.dicoding.moviecatalog.databinding.FragmentTvShowBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.ui.view.main.MainViewModel
import com.dicoding.moviecatalog.utils.GridSpacingItemDecoration

class TvShowFragment : Fragment() {
    private var filmAdapter = FilmSmallListAdapter(
        object: FilmListInterface {
            override fun onItemClicked(item: FilmModel) {
                val intent = Intent(requireContext(), DetailFilmActivity::class.java)
                intent.putExtra(DetailFilmActivity.FILM, item)
                startActivity(intent)
            }
        }
    )
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTvShowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTvShowBinding.bind(view)

        binding.rvList.apply{
            adapter = filmAdapter
            val span = if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
            layoutManager = GridLayoutManager(requireContext(), span)
            val spaces = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
            val decoration = GridSpacingItemDecoration(span, spaces, true, 0)
            addItemDecoration(decoration)
        }

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.setSeries()
        viewModel.series.observe(viewLifecycleOwner){
            filmAdapter.setData(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TvShowFragment()
    }
}