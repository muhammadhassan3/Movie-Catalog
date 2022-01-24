package com.dicoding.moviecatalog.data.interactor

import androidx.lifecycle.LiveData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.repository.FilmRepository
import com.dicoding.moviecatalog.data.usecase.FilmDetailUseCase
import com.dicoding.moviecatalog.utils.ApiResponse

class FilmDetailInteractor(private val repository: FilmRepository): FilmDetailUseCase {
    override fun getDetailMovie(id: Int): LiveData<ApiResponse<FilmModel>> {
        return repository.loadMoviesDetail(id)
    }

    override fun getDetailSeries(id: Int): LiveData<ApiResponse<FilmModel>> {
        return repository.loadSeriesDetail(id)
    }
}