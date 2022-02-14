package com.dicoding.moviecatalog.ui.view.favorite.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.moviecatalog.adapter.FilmFavoriteMediumListAdapter
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.FragmentFavoriteMoviesBinding
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmActivity
import com.dicoding.moviecatalog.utils.gone
import com.dicoding.moviecatalog.utils.visible
import org.koin.android.ext.android.inject

class FavoriteMoviesFragment : Fragment(), FilmListInterface {
    private val viewModel: FavoriteMoviesViewModel by inject()
    private lateinit var filmFavoriteAdapter: FilmFavoriteMediumListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoriteMoviesBinding.bind(view)

        initRecyclerView(binding)
        initViewModel(binding)
    }

    private fun initViewModel(binding: FragmentFavoriteMoviesBinding) {
        viewModel.getData().observe(viewLifecycleOwner){
            filmFavoriteAdapter.submitData(lifecycle, it)
        }

        viewModel.getSize().observe(viewLifecycleOwner){
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

    private fun initRecyclerView(binding: FragmentFavoriteMoviesBinding) {
        filmFavoriteAdapter = FilmFavoriteMediumListAdapter(requireContext(), this)
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = filmFavoriteAdapter
        }
    }

    override fun onItemClicked(item: FilmModel) {
        val intent = Intent(requireContext(), DetailFilmActivity::class.java).apply{
            putExtra(DetailFilmActivity.ID, item.id)
            putExtra(DetailFilmActivity.MEDIA_TYPE, item.type)
        }
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoriteMoviesFragment()
    }
}