package com.dicoding.moviecatalog.ui.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.usecase.FilmListUseCase
import com.dicoding.moviecatalog.utils.ApiResponse

class MainViewModel(private val useCase: FilmListUseCase): ViewModel() {
    lateinit var movies: LiveData<ApiResponse<List<FilmModel>>>
    lateinit var series: LiveData<ApiResponse<List<FilmModel>>>
    lateinit var random: LiveData<ApiResponse<List<FilmModel>>>

    fun setMovies(){
        movies = useCase.getPopularMovies()
    }

    fun setSeries() {
        series = useCase.getPopularSeries()
    }

    fun setRandom(size: Int) {
        random = useCase.getPopularFilm(size)
    }
}