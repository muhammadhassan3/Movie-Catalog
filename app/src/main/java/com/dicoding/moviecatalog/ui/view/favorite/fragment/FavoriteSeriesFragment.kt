package com.dicoding.moviecatalog.ui.view.favorite.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.moviecatalog.adapter.FilmFavoriteMediumListAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.FragmentFavoriteSeriesBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.utils.gone
import com.dicoding.moviecatalog.utils.visible
import org.koin.android.ext.android.inject

class FavoriteSeriesFragment : Fragment(), FilmListInterface {
    private val viewModel: FavoriteSeriesViewModel by inject()
    private lateinit var filmFavoriteAdapter: FilmFavoriteMediumListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoriteSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoriteSeriesBinding.bind(view)

        initRecyclerView(binding)
        intiViewModel(binding)
    }
    private fun intiViewModel(binding: FragmentFavoriteSeriesBinding) {
        viewModel.getData().observe(viewLifecycleOwner) { data ->
            filmFavoriteAdapter.submitData(lifecycle, data)
        }

        viewModel.getSize().observe(viewLifecycleOwner) {
            if (it > 0) {
                binding.apply {
                    noData.root.gone()
                    rvList.visible()
                }
            } else {
                binding.apply {
                    noData.root.visible()
                    rvList.gone()
                }
            }
        }
    }

    private fun initRecyclerView(binding: FragmentFavoriteSeriesBinding) {
        filmFavoriteAdapter = FilmFavoriteMediumListAdapter(requireContext(), this)
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = filmFavoriteAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoriteSeriesFragment()
    }

    override fun onItemClicked(item: FilmModel) {
        val intent = Intent(requireContext(), DetailFilmActivity::class.java).apply{
            putExtra(DetailFilmActivity.ID, item.id)
            putExtra(DetailFilmActivity.MEDIA_TYPE, item.type)
        }
        startActivity(intent)
    }
}