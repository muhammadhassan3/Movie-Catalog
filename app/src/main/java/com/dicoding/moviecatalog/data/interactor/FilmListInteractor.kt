package com.dicoding.moviecatalog.data.interactor

import androidx.lifecycle.LiveData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.repository.FilmRepository
import com.dicoding.moviecatalog.data.usecase.FilmListUseCase
import com.dicoding.moviecatalog.utils.ApiResponse

class FilmListInteractor(private val repository: FilmRepository): FilmListUseCase {
    override fun getPopularFilm(size: Int): LiveData<ApiResponse<List<FilmModel>>> {
        return repository.loadRandom(size)
    }

    override fun getPopularMovies(): LiveData<ApiResponse<List<FilmModel>>> {
        return repository.loadMovies()
    }

    override fun getPopularSeries(): LiveData<ApiResponse<List<FilmModel>>> {
        return repository.loadSeries()
    }
}