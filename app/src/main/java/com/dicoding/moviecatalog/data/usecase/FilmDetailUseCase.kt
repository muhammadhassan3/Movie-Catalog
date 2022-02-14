package com.dicoding.moviecatalog.data.usecase

import androidx.lifecycle.LiveData
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.utils.ApiResponse

interface FilmDetailUseCase {
    fun getDetailMovie(id: Int): LiveData<ApiResponse<FilmModel>>
    fun getDetailSeries(id: Int): LiveData<ApiResponse<FilmModel>>
    fun getFavoriteById(id: Long): LiveData<FavoriteEntity?>
    fun deleteFavoriteById(id: Long)
    fun insertFavorite(favorite: FavoriteEntity)
}