package com.dicoding.moviecatalog.ui.view.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.ActivityDetailFilmBinding
import com.dicoding.moviecatalog.utils.*
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFilmBinding
    private var id: Int = 0
    private var mediaType: String? = null
    private val viewModel: DetailFilmViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState != null) {
            id = savedInstanceState.getInt(ID, 0)
            mediaType = savedInstanceState.getString(MEDIA_TYPE)
        } else {
            id = intent.getIntExtra(ID, 0)
            mediaType = intent.getStringExtra(MEDIA_TYPE)
        }


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(
                R.string.details_title,
                getString(if (mediaType == "movie") R.string.film else R.string.series)
            )
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        initViewModel()
    }

    private fun initViewModel() {
        mediaType?.let { viewModel.setData(id, it) }
        viewModel.data.observe(this) {
            Log.e("View Model", (it==null).toString())
            when(it.status){
                Status.NO_DATA -> {
                    showState(State.NO_DATA)
                }
                Status.LOADING -> {
                    showState(State.LOADING)
                }
                Status.ERROR -> {
                    showState(State.NO_DATA)
                    val content = it.message?.getContentIfNotHandled()
                    content?.let{ message ->
                        binding.root.showSnackbar(message)
                    }
                }
                Status.SUCCESS -> {
                    it.data?.let{ data ->
                        showState(State.SUCCESS)
                        initView(data)
                    }
                }
            }
        }
    }

    private fun initView(film: FilmModel) {
        binding.apply {
            imgPoster.loadImage(Constant.ImageBaseUrl+film.poster, film.poster)
            tvTitle.text = film.title ?: film.title
            tvReleaseDate.text = film.releaseDate

            tvRating.text = film.rating.toString()
            rating.rating = film.rating / 2

            for (genre in film.genres) {
                val chip = Chip(this@DetailFilmActivity)
                chip.text = genre
                chip.setChipBackgroundColorResource(R.color.white)
                chip.setChipStrokeColorResource(android.R.color.darker_gray)
                chip.chipStrokeWidth = 1f
                chip.isClickable = false
                chipGroup.addView(chip)
            }

            tvSynopsis.text = film.synopsis
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ID, id)
        outState.putString(MEDIA_TYPE, mediaType)
        super.onSaveInstanceState(outState)
    }

    private fun showState(state: State){
        when(state){
            State.SUCCESS -> {
                binding.apply {
                    shimmerDetail.apply{
                        stopShimmer()
                        gone()
                    }

                    containerDetail.visible()
                    containerSynopsis.visible()
                    emptyData.root.gone()
                }
            }
            State.NO_DATA -> {
                binding.apply{
                    shimmerDetail.apply {
                        stopShimmer()
                        gone()
                    }
                    containerSynopsis.gone()
                    containerDetail.gone()
                    emptyData.root.visible()
                }
            }
            State.LOADING -> {
                binding.apply{
                    shimmerDetail.apply {
                        startShimmer()
                        visible()
                    }
                    containerDetail.visible()
                    containerSynopsis.visible()
                    emptyData.root.gone()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerDetail.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerDetail.startShimmer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val ID = "id"
        const val MEDIA_TYPE = "media_type"
    }
}