package com.dicoding.moviecatalog.ui.view.main

import androidx.lifecycle.ViewModel
import com.dicoding.moviecatalog.data.repository.FilmRepository

class MainViewModel: ViewModel() {
    private val repository = FilmRepository()
    val movies = repository.movies
    val series = repository.series
    val random = repository.random

    fun setMovies() = repository.setMovies()

    fun setSeries() = repository.setSeries()

    fun setRandom(size: Int) = repository.setRandom(size)
}