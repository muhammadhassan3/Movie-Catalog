package com.dicoding.moviecatalog.data.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.utils.ApiResponse
import com.dicoding.moviecatalog.utils.ProcessState
import kotlinx.coroutines.flow.Flow

interface FilmListUseCase {
    fun getPopularFilm(): Flow<PagingData<FilmModel>>
    fun getPopularMovies(): Flow<PagingData<FilmModel>>
    fun getPopularSeries(): Flow<PagingData<FilmModel>>
}