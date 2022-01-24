package com.dicoding.moviecatalog.data.usecase

import androidx.lifecycle.LiveData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.utils.ApiResponse

interface FilmListUseCase {
    fun getPopularFilm(size: Int): LiveData<ApiResponse<List<FilmModel>>>
    fun getPopularMovies(): LiveData<ApiResponse<List<FilmModel>>>
    fun getPopularSeries(): LiveData<ApiResponse<List<FilmModel>>>
}