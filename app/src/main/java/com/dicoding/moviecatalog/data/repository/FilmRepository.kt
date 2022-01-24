package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.utils.ApiResponse

interface FilmRepository {
    fun loadRandom(size: Int): LiveData<ApiResponse<List<FilmModel>>>
    fun loadMovies(): LiveData<ApiResponse<List<FilmModel>>>
    fun loadSeries(): LiveData<ApiResponse<List<FilmModel>>>
    fun loadMoviesDetail(id: Int): LiveData<ApiResponse<FilmModel>>
    fun loadSeriesDetail(id: Int): LiveData<ApiResponse<FilmModel>>
}