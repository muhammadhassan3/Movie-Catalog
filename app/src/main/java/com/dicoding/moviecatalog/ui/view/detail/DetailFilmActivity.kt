package com.dicoding.moviecatalog.ui.view.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.ActivityDetailFilmBinding
import com.dicoding.moviecatalog.utils.loadImage
import com.google.android.material.chip.Chip

class DetailFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFilmBinding
    private var film: FilmModel? = null
    private val viewModel by viewModels<DetailFilmViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        film = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(FILM)
        } else intent.getParcelableExtra(FILM)

        viewModel.setData(film)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.details_title, film?.type?.type)
        }

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.data.observe(this) {
            it?.let {
                initView(it)
            }
        }
    }

    private fun initView(film: FilmModel) {
        binding.apply {
            imgPoster.loadImage(film.poster, true, film.poster)
            tvTitle.text = film.title
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
        outState.putParcelable(FILM, film)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val FILM = "film"
    }
}