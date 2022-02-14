package com.dicoding.moviecatalog.data.interactor

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.repository.FilmRepository
import com.dicoding.moviecatalog.data.usecase.FilmListUseCase
import com.dicoding.moviecatalog.utils.ApiResponse
import com.dicoding.moviecatalog.utils.ProcessState
import kotlinx.coroutines.flow.Flow

class FilmListInteractor(private val repository: FilmRepository): FilmListUseCase {

    override fun getPopularFilm(): Flow<PagingData<FilmModel>> {
        return repository.loadTrending()
    }

    override fun getPopularMovies(): Flow<PagingData<FilmModel>> {
        return repository.loadMovies()
    }

    override fun getPopularSeries(): Flow<PagingData<FilmModel>> {
        return repository.loadSeries()
    }
}