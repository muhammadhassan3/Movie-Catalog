package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface FilmRepository {
    fun loadTrending(): Flow<PagingData<FilmModel>>
    fun loadMovies(): Flow<PagingData<FilmModel>>
    fun loadSeries(): Flow<PagingData<FilmModel>>
    fun getSearchResult(query: String): LiveData<PagingData<FilmModel>>
    fun loadMoviesDetail(id: Int): LiveData<ApiResponse<FilmModel>>
    fun loadSeriesDetail(id: Int): LiveData<ApiResponse<FilmModel>>
}