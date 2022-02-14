package com.dicoding.moviecatalog.data.interactor

import androidx.lifecycle.LiveData
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.repository.FavoriteRepository
import com.dicoding.moviecatalog.data.repository.FilmRepository
import com.dicoding.moviecatalog.data.usecase.FilmDetailUseCase
import com.dicoding.moviecatalog.utils.ApiResponse

class FilmDetailInteractor(private val repository: FilmRepository, private val favRepository: FavoriteRepository): FilmDetailUseCase {
    override fun getDetailMovie(id: Int): LiveData<ApiResponse<FilmModel>> {
        return repository.loadMoviesDetail(id)
    }

    override fun getDetailSeries(id: Int): LiveData<ApiResponse<FilmModel>> {
        return repository.loadSeriesDetail(id)
    }

    override fun getFavoriteById(id: Long): LiveData<FavoriteEntity?> {
        return favRepository.getFavoriteById(id)
    }

    override fun deleteFavoriteById(id: Long) {
        favRepository.deleteFavoriteById(id)
    }

    override fun insertFavorite(favorite: FavoriteEntity) {
        favRepository.insert(favorite)
    }
}